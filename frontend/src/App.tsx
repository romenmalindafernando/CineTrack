import { useCallback, useEffect, useState } from "react";
import axios from "axios";
import {
  searchMovies,
  searchTvShows,
  getUserStats,
  getYearlyStats,
  getViewingHistory,
  getMovieDetails,
  getTvDetails,
  addToWatched,
  removeFromWatched,
  removeWatchedEpisode,
} from "./services/cinetrackApi";
import type {
  UserStats,
  YearlyStats,
  ViewingHistoryRecord,
} from "./services/cinetrackApi";
import "./App.css";

import { movies, type Movie } from "./data/movies";
import * as tvShowData from "./data/tvShows";
import Sidebar from "./components/Sidebar";
import Topbar from "./components/Topbar";
import MovieCard from "./components/MovieCard";
import MovieDetails from "./components/MovieDetails";
import TvDetails from "./components/TvDetails";

const tvShows = Object.values(tvShowData).find(Array.isArray) as
  | typeof movies
  | undefined ?? [];

const DASHBOARD_USER_ID = "6ac9f4a41aaaa43b220d0fe1";

function App() {
  const [activePage, setActivePage] = useState("Discover");
  const [search, setSearch] = useState("");
  const [selectedMovieId, setSelectedMovieId] = useState<number | null>(null);
  const [selectedTvId, setSelectedTvId] = useState<number | null>(null);
  const [apiMovies, setApiMovies] = useState<typeof movies>([]);
  const [isSearching, setIsSearching] = useState(false);
  const [searchError, setSearchError] = useState("");

  const [backendStats, setBackendStats] = useState<UserStats | null>(null);
  const [backendYearlyStats, setBackendYearlyStats] =
    useState<YearlyStats | null>(null);
  const [backendHistory, setBackendHistory] =
    useState<ViewingHistoryRecord[]>([]);
  const [removingHistoryKey, setRemovingHistoryKey] = useState<string | null>(null);
  const [resolvedBackendHistory, setResolvedBackendHistory] =
    useState<Array<{ record: ViewingHistoryRecord; title: Movie }>>([]);
  const [dashboardLoading, setDashboardLoading] = useState(true);
  const [dashboardError, setDashboardError] = useState("");

  const refreshDashboardData = useCallback(async () => {
    setDashboardLoading(true);
    setDashboardError("");

    try {
      const currentYear = new Date().getFullYear();

      const [stats, yearly, history] = await Promise.all([
        getUserStats(DASHBOARD_USER_ID),
        getYearlyStats(DASHBOARD_USER_ID, currentYear),
        getViewingHistory(DASHBOARD_USER_ID),
      ]);

      setBackendStats(stats);
      setBackendYearlyStats(yearly);
      setBackendHistory(history);
    } catch {
      setDashboardError(
        "Backend statistics are unavailable. Real viewing totals cannot currently be refreshed.",
      );
    } finally {
      setDashboardLoading(false);
    }
  }, []);

  useEffect(() => {
    void refreshDashboardData();
  }, [refreshDashboardData]);

  useEffect(() => {
    let cancelled = false;

    async function resolveHistoryTitles() {
      const resolved = await Promise.all(
        backendHistory.map(async (record) => {
          try {
            if (record.mediaType === "tv_episode") {
              const details = await getTvDetails(record.tmdbId);

              return {
                record,
                title: {
                  id: details.id,
                  title: details.name,
                  year: details.firstAirDate
                    ? Number(details.firstAirDate.slice(0, 4))
                    : 0,
                  rating: details.rating ?? 0,
                  genre: details.genres?.[0] ?? "TV Series",
                  poster: details.posterPath
                    ? `https://image.tmdb.org/t/p/w500${details.posterPath}`
                    : "https://placehold.co/500x750/17152b/ffffff?text=No+Poster",
                } satisfies Movie,
              };
            }

            const details = await getMovieDetails(record.tmdbId);

            return {
              record,
              title: {
                id: details.id,
                title: details.title,
                year: details.releaseDate
                  ? Number(details.releaseDate.slice(0, 4))
                  : 0,
                rating: details.rating ?? 0,
                genre: details.genres?.[0] ?? "Movie",
                poster: details.posterPath
                  ? `https://image.tmdb.org/t/p/w500${details.posterPath}`
                  : "https://placehold.co/500x750/17152b/ffffff?text=No+Poster",
              } satisfies Movie,
            };
          } catch {
            return {
              record,
              title: {
                id: record.tmdbId,
                title: record.mediaType === "tv_episode"
                  ? `TV Series (${record.tmdbId})`
                  : `Movie (${record.tmdbId})`,
                year: 0,
                rating: 0,
                genre: record.mediaType === "tv_episode"
                  ? "TV Series"
                  : "Movie",
                poster: "https://placehold.co/500x750/17152b/ffffff?text=No+Poster",
              } satisfies Movie,
            };
          }
        }),
      );

      if (!cancelled) {
        setResolvedBackendHistory(resolved);
      }
    }

    void resolveHistoryTitles();

    return () => {
      cancelled = true;
    };
  }, [backendHistory]);

  useEffect(() => {
    const query = search.trim();

    if (!query) {
      setApiMovies([]);
      setIsSearching(false);
      setSearchError("");
      return;
    }

    if (!["Discover", "Movies", "TV Shows"].includes(activePage)) {
      setIsSearching(false);
      setSearchError("");
      return;
    }

    let cancelled = false;
    setIsSearching(true);
    setSearchError("");

    const timer = window.setTimeout(async () => {
      try {
        const results =
          activePage === "TV Shows"
            ? await searchTvShows(query)
            : await searchMovies(query);

        if (!cancelled) {
          setApiMovies(results);
        }
      } catch (error) {
        if (!cancelled) {
          setApiMovies([]);
          setSearchError(
            axios.isAxiosError(error)
              ? "Unable to load results. Check that the backend is running and try again."
              : "Something went wrong while searching. Please try again.",
          );
        }
      } finally {
        if (!cancelled) {
          setIsSearching(false);
        }
      }
    }, 350);

    return () => {
      cancelled = true;
      window.clearTimeout(timer);
    };
  }, [search, activePage]);

  const [savedMovies, setSavedMovies] = useState<number[]>(() => {
    try {
      const saved = localStorage.getItem("cinetrack-movie-watchlist");
      if (saved) return JSON.parse(saved) as number[];

      const legacy = JSON.parse(
        localStorage.getItem("cinetrack-watchlist") ?? "[]",
      ) as number[];

      const oldMovieIds = new Map<number, number>([
        [1, 157336],
        [2, 693134],
        [3, 414906],
        [4, 872585],
        [5, 569094],
        [6, 27205],
      ]);

      const oldTvIds = new Set([101, 102, 103, 104]);

      return [...new Set(
        legacy
          .filter((id) => !oldTvIds.has(id))
          .filter((id) => !tvShows.some((show) => show.id === id))
          .map((id) => oldMovieIds.get(id) ?? id),
      )];
    } catch {
      return [];
    }
  });

  const [savedTvShows, setSavedTvShows] = useState<number[]>(() => {
    try {
      const saved = localStorage.getItem("cinetrack-tv-watchlist");
      if (saved) return JSON.parse(saved) as number[];

      const legacy = JSON.parse(
        localStorage.getItem("cinetrack-watchlist") ?? "[]",
      ) as number[];

      const oldTvIds = new Map<number, number>([
        [101, 1396],
        [102, 66732],
        [103, 100088],
        [104, 119051],
      ]);

      return [...new Set([
        ...legacy.filter((id) => tvShows.some((show) => show.id === id)),
        ...legacy.flatMap((id) =>
          oldTvIds.has(id) ? [oldTvIds.get(id)!] : [],
        ),
      ])];
    } catch {
      return [];
    }
  });

  const [cachedTitles, setCachedTitles] = useState<typeof movies>(() => {
    try {
      const cached = localStorage.getItem("cinetrack-cached-titles");
      return cached ? (JSON.parse(cached) as typeof movies) : [];
    } catch {
      return [];
    }
  });

  const [watchedTitles, setWatchedTitles] = useState<
    Array<{ id: number; type: "movie" | "tv"; watchedAt: string }>
  >(() => {
    try {
      const stored = localStorage.getItem("cinetrack-watched-history");
      return stored
        ? (JSON.parse(stored) as Array<{
            id: number;
            type: "movie" | "tv";
            watchedAt: string;
          }>)
        : [];
    } catch {
      return [];
    }
  });

  const [activeGenre, setActiveGenre] = useState("All");

  const navigateToPage = (page: string) => {
    setActivePage(page);
    setActiveGenre("All");
    setSearch("");
  };

  useEffect(() => {
    localStorage.setItem(
      "cinetrack-movie-watchlist",
      JSON.stringify(savedMovies),
    );
  }, [savedMovies]);

  useEffect(() => {
    localStorage.setItem(
      "cinetrack-tv-watchlist",
      JSON.stringify(savedTvShows),
    );
  }, [savedTvShows]);

  useEffect(() => {
    localStorage.setItem(
      "cinetrack-watched-history",
      JSON.stringify(watchedTitles),
    );
  }, [watchedTitles]);

  useEffect(() => {
    localStorage.setItem(
      "cinetrack-cached-titles",
      JSON.stringify(cachedTitles),
    );
  }, [cachedTitles]);

  useEffect(() => {
    if (apiMovies.length === 0) return;

    setCachedTitles((current) => {
      const combined = [...current];

      for (const item of apiMovies) {
        const index = combined.findIndex(
          (existing) =>
            existing.id === item.id && existing.genre === item.genre,
        );

        if (index >= 0) {
          combined[index] = item;
        } else {
          combined.push(item);
        }
      }

      return combined;
    });
  }, [apiMovies]);

  const filteredMovies = movies.filter((movie) => {
    const matchesSearch = movie.title
      .toLowerCase()
      .includes(search.toLowerCase());

    const matchesGenre =
      activeGenre === "All" || movie.genre === activeGenre;

    return matchesSearch && matchesGenre;
  });

  const toggleWatchlist = (id: number) => {
    setSavedMovies((current) =>
      current.includes(id)
        ? current.filter((movieId) => movieId !== id)
        : [...current, id],
    );
  };

  const toggleTvWatchlist = (id: number) => {
    setSavedTvShows((current) =>
      current.includes(id)
        ? current.filter((showId) => showId !== id)
        : [...current, id],
    );
  };


  const toggleWatched = async (id: number, type: "movie" | "tv") => {
    const currentlyWatched = watchedTitles.some(
      (entry) => entry.id === id && entry.type === type,
    );

    // The built-in sample data must never create real viewing records.
    const isLocalPlaceholder =
      type === "movie"
        ? movies.some((item) => item.id === id)
        : tvShows.some((item) => item.id === id);

    try {
      if (!isLocalPlaceholder) {
        if (currentlyWatched) {
          await removeFromWatched(DASHBOARD_USER_ID, id, type);
        } else {
          await addToWatched(DASHBOARD_USER_ID, id, type);
        }
      }

      setWatchedTitles((current) => {
        if (currentlyWatched) {
          return current.filter(
            (entry) => !(entry.id === id && entry.type === type),
          );
        }

        return [...current, { id, type, watchedAt: new Date().toISOString() }];
      });

      if (!isLocalPlaceholder) {
        await refreshDashboardData();
      }
    } catch {
      setDashboardError(
        "Could not update watched status. Check the backend connection and try again.",
      );
    }
  };

  const removeWatchedHistory = (id: number, type: "movie" | "tv") => {
    setWatchedTitles((current) =>
      current.filter((entry) => !(entry.id === id && entry.type === type)),
    );
  };


  const removeBackendHistoryRecord = async (
    record: ViewingHistoryRecord,
  ) => {
    const key = `${record.tmdbId}-${record.mediaType}-${record.seasonNumber ?? "x"}-${record.episodeNumber ?? "x"}-${record.watchedAt}`;

    if (!window.confirm("Remove this item from your viewing history?")) {
      return;
    }

    setRemovingHistoryKey(key);

    try {
      console.log("[CineTrack] Removing history record:", record);

      if (
        record.mediaType === "movie" ||
        record.mediaType === "tv"
      ) {
        await removeFromWatched(
          DASHBOARD_USER_ID,
          record.tmdbId,
          record.mediaType,
        );
      } else if (record.mediaType === "tv_episode") {
        if (
          record.seasonNumber == null ||
          record.episodeNumber == null
        ) {
          throw new Error("Episode season or number is missing.");
        }

        await removeWatchedEpisode(
          DASHBOARD_USER_ID,
          record.tmdbId,
          record.seasonNumber,
          record.episodeNumber,
        );
      } else {
        throw new Error(
          `Unsupported history media type: ${record.mediaType}`,
        );
      }

      await refreshDashboardData();
      console.log("[CineTrack] History removal and refresh completed.");
    } catch (error: unknown) {
      console.error("[CineTrack] History removal failed:", error);

      if (error && typeof error === "object" && "response" in error) {
        const response = (
          error as {
            response?: {
              status?: number;
              data?: unknown;
            };
          }
        ).response;

        console.error("[CineTrack] HTTP status:", response?.status);
        console.error("[CineTrack] Server response:", response?.data);
      }

      const message =
        error instanceof Error ? error.message : String(error);

      window.alert(
        `Could not remove this item. Details: ${message}\nOpen the browser console (F12) for more information.`,
      );
    } finally {
      setRemovingHistoryKey(null);
    }
  };

const allMovieCards = [
  ...movies,
  ...cachedTitles.filter((item) => item.genre !== "TV Series"),
];

const allTvCards = [
  ...tvShows,
  ...cachedTitles.filter((item) => item.genre === "TV Series"),
];

const localTvShows = allTvCards
  .filter(
    (show, index, items) =>
      items.findIndex((item) => item.id === show.id) === index,
  )
  .filter((show) => {
    const matchesSearch = show.title
      .toLowerCase()
      .includes(search.toLowerCase());

    const matchesGenre =
      activeGenre === "All" || show.genre === activeGenre;

    return matchesSearch && matchesGenre;
  });

const displayedMovies =
  search.trim() && ["Discover", "Movies", "TV Shows"].includes(activePage)
    ? apiMovies
    : activePage === "My Library"
      ? [
          ...allMovieCards
            .filter((movie) => savedMovies.includes(movie.id))
            .filter(
              (movie, index, items) =>
                items.findIndex((item) => item.id === movie.id) === index,
            )
            .filter((movie) => {
              const matchesSearch = movie.title
                .toLowerCase()
                .includes(search.toLowerCase());
              const matchesGenre =
                activeGenre === "All" || movie.genre === activeGenre;
              return matchesSearch && matchesGenre;
            }),
          ...localTvShows.filter((show) => savedTvShows.includes(show.id)),
        ]
      : activePage === "TV Shows"
        ? localTvShows
        : filteredMovies;

const statisticsMonths = Array.from({ length: 6 }, (_, index) => {
  const date = new Date();
  date.setDate(1);
  date.setMonth(date.getMonth() - (5 - index));

  const key = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}`;
  const backendMonth = backendYearlyStats?.monthlyBreakdown.find(
    (month) => month.month === key,
  );

  return {
    key,
    label: date.toLocaleDateString(undefined, { month: "short" }),
    count: backendYearlyStats ? (backendMonth?.totalActivities ?? 0) : 0,
  };
});

const maxMonthlyCount = Math.max(
  1,
  ...statisticsMonths.map((month) => month.count),
);

const watchedMovieCount = backendStats?.moviesWatched ?? 0;

const watchedTvCount = backendStats?.episodesWatched ?? 0;

const totalActivities = backendYearlyStats?.totalActivities ?? 0;

const totalViewingMinutes =
  backendYearlyStats?.totalViewingMinutes ?? 0;

const formatViewingTime = (minutes: number) => {
  const hours = Math.floor(minutes / 60);
  const remainingMinutes = minutes % 60;
  return hours > 0
    ? `${hours}h ${remainingMinutes}m`
    : `${remainingMinutes}m`;
};

const totalWatchlistCount =
  backendStats
    ? backendStats.moviesInWatchlist + backendStats.tvSeriesInWatchlist
    : new Set([
        ...savedMovies.map((id) => `movie-${id}`),
        ...savedTvShows.map((id) => `tv-${id}`),
      ]).size;
const sectionTitle =
  activePage === "My Library"
    ? "My Watchlist"
    : activePage === "Movies"
      ? "Explore Movies"
      : activePage === "TV Shows"
        ? "Explore TV Shows"
        : activePage === "Statistics"
          ? "Your Statistics"
          : "Trending this week";


  return (
    <div className="app-shell">
     <Sidebar
  activePage={activePage}
  savedCount={savedMovies.length + savedTvShows.length}
  historyCount={backendHistory.length > 0 ? backendHistory.length : watchedTitles.length}
  onNavigate={navigateToPage}
/>

      <main className="main-content">
        <Topbar
          activePage={activePage}
          search={search}
          onSearchChange={setSearch}
        />

        <div className="page-content">
          <section className="welcome-row">
            <div>
              <p className="eyebrow">
                <span className="eyebrow-dot" /> YOUR CINEMATIC JOURNEY
              </p>

              <h1>
                Discover your next
                <br />
                <span className="gradient-text">favourite story.</span>
              </h1>

              <p className="welcome-description">
                Explore extraordinary films, track your journey, and build
                your personal movie universe.
              </p>
            </div>

            <div className="welcome-decoration" aria-hidden="true">
              <div className="decoration-ring ring-one" />
              <div className="decoration-ring ring-two" />
              <div className="decoration-center">C</div>
              <span className="decoration-star star-one">✦</span>
              <span className="decoration-star star-two">{"\u2727"}</span>
            </div>
          </section>

          <section className="stats-row">
            <div className="stat-card">
              <span className="stat-icon purple">▣</span>
              <div>
                <p>IN YOUR WATCHLIST</p>
                <h3>
                  {savedMovies.length.toString().padStart(2, "0")}
                </h3>
              </div>
            </div>

            <div className="stat-card">
              <span className="stat-icon pink">▶</span>
              <div>
                <p>TITLES TO EXPLORE</p>
                <h3>{movies.length.toString().padStart(2, "0")}</h3>
              </div>
            </div>

            <div className="stat-card">
              <span className="stat-icon yellow">★</span>
              <div>
                <p>TOP COMMUNITY RATING</p>
                <h3>
                  8.8 <span className="stat-suffix">/ 10</span>
                </h3>
              </div>
            </div>
          </section>

          {activePage !== "TV Shows" && (
            <section className="featured-section">
            <div className="section-heading">
              <div>
                <p className="eyebrow">EDITOR'S SPOTLIGHT</p>
                <h2>Featured film</h2>
              </div>

              <span className="featured-label">
                ✦ HANDPICKED FOR YOU
              </span>
            </div>

            <div className="featured-card">
              <img
                className="featured-backdrop"
                src="https://image.tmdb.org/t/p/w1280/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg"
                alt=""
              />

              <div className="featured-overlay" />

              <div className="featured-details">
                <span className="featured-tag">FEATURED MOVIE</span>

                <h2>Interstellar</h2>

                <div className="featured-meta">
                  <span>2014</span>
                  <span>{"\u2022"}</span>
                  <span>2h 49m</span>
                  <span>{"\u2022"}</span>
                  <span>Sci-Fi</span>
                </div>

                <p>
                  Mankind's future lies beyond the stars. One journey will
                  push the boundaries of space, time, and human connection.
                </p>

                <div className="featured-actions">
                  <button
                    className="primary-button"
                    onClick={() => toggleWatchlist(movies[0].id)}
                  >
                    {savedMovies.includes(movies[0].id)
                      ? "\u2713 In My Watchlist"
                      : "+ Add to Watchlist"}
                  </button>

                  <button
                    className="primary-button"
                    onClick={() => {
                      setSelectedTvId(null);
                      setSelectedMovieId(movies[0].id);
                    }}
                  >
                    View Details
                  </button>

                  <span className="featured-rating">
                    ★ 8.7 <small>/ 10</small>
                  </span>
                </div>
              </div>

              <div className="featured-poster">
                <img
                  src={movies[0].poster}
                  alt="Interstellar poster"
                />
              </div>
            </div>
          </section>
          )}

          <section className="movies-section">
            <div className="section-heading movie-heading">
              <div>
                <p className="eyebrow">FIND SOMETHING AMAZING</p>
                <h2>{sectionTitle}</h2>
              </div>

              <div className="genre-filters">
                {[
                  "All",
                  "Sci-Fi",
                  "Action",
                  "Drama",
                  "Adventure",
                  "Animation",
                ].map((genre) => (
                  <button
                    key={genre}
                    className={`genre-chip ${
                      activeGenre === genre ? "selected" : ""
                    }`}
                    onClick={() => setActiveGenre(genre)}
                  >
                    {genre}
                  </button>
                ))}
              </div>
            </div>

            {isSearching ? (
              <div className="empty-state">
                <h3>Searching CineTrack...</h3>
                <p>Finding titles from the movie and TV database.</p>
              </div>
            ) : searchError ? (
              <div className="empty-state">
                <h3>Search unavailable</h3>
                <p>{searchError}</p>
              </div>
            ) : activePage === "Statistics" ? (
              <div className="statistics-dashboard">
                <div className="statistics-intro">
                  <p className="eyebrow">YOUR PERSONAL INSIGHTS</p>
                  <h2>Your viewing journey</h2>
                  <p>Explore your watched titles and activity across CineTrack.</p>
                  {dashboardLoading && (
                    <p className="statistics-note">Loading your statistics from CineTrack...</p>
                  )}
                  {!dashboardLoading && dashboardError && (
                    <p className="statistics-note">{dashboardError}</p>
                  )}
                  {!dashboardLoading && !dashboardError && (
                    <p className="statistics-note">
                      Connected to the backend: {backendHistory.length} viewing history records loaded.
                    </p>
                  )}
                </div>

                <div className="statistics-summary">
                  <div className="stat-card">
                    <span className="stat-icon purple">{"\u2713"}</span>
                    <div>
                      <p>TOTAL ACTIVITIES</p>
                      <h3>{totalActivities.toString().padStart(2, "0")}</h3>
                    </div>
                  </div>

                  <div className="stat-card">
                    <span className="stat-icon pink">{"\u25A3"}</span>
                    <div>
                      <p>MOVIES WATCHED</p>
                      <h3>{watchedMovieCount.toString().padStart(2, "0")}</h3>
                    </div>
                  </div>

                  <div className="stat-card">
                    <span className="stat-icon yellow">{"\u25B6"}</span>
                    <div>
                      <p>TV EPISODES WATCHED</p>
                      <h3>{watchedTvCount.toString().padStart(2, "0")}</h3>
                    </div>
                  </div>

                  <div className="stat-card">
                    <span className="stat-icon purple">{"\u2661"}</span>
                    <div>
                      <p>IN YOUR WATCHLIST</p>
                      <h3>{totalWatchlistCount.toString().padStart(2, "0")}</h3>
                    </div>
                  </div>
                  <div className="stat-card">
                    <span className="stat-icon pink">{"\u23F1"}</span>
                    <div>
                      <p>TOTAL VIEWING TIME</p>
                      <h3>{formatViewingTime(totalViewingMinutes)}</h3>
                    </div>
                  </div>
                </div>

                <div className="statistics-panels">
                  <section className="statistics-panel">
                    <div className="statistics-panel-heading">
                      <div>
                        <p className="eyebrow">YOUR CONSISTENCY</p>
                        <h3>Viewing activity</h3>
                      </div>
                      <span className="statistics-period">LAST 6 MONTHS</span>
                    </div>

                    {totalActivities === 0 ? (
                      <div className="statistics-empty">
                        <p>Your activity chart will appear when you mark titles as watched.</p>
                        <button className="primary-button" onClick={() => navigateToPage("Discover")}>
                          Explore titles
                        </button>
                      </div>
                    ) : (
                      <div className="activity-chart">
                        {statisticsMonths.map((month) => (
                          <div className="activity-column" key={month.key}>
                            <span className="activity-count">{month.count || ""}</span>
                            <div className="activity-bar-track">
                              <div
                                className="activity-bar"
                                style={{
                                  height: `${month.count === 0 ? 3 : Math.max(8, (month.count / maxMonthlyCount) * 100)}%`,
                                }}
                                title={`${month.count} title${month.count === 1 ? "" : "s"} marked watched`}
                              />
                            </div>
                            <span className="activity-month">{month.label}</span>
                          </div>
                        ))}
                      </div>
                    )}
                    <p className="statistics-note">Based on the dates you marked titles as watched.</p>
                  </section>

                  <section className="statistics-panel">
                    <div className="statistics-panel-heading">
                      <div>
                        <p className="eyebrow">YOUR COLLECTION</p>
                        <h3>Movies vs TV</h3>
                      </div>
                    </div>

                    <div className="breakdown-total">
                      <strong>{totalActivities}</strong>
                      <span>watched titles</span>
                    </div>

                    <div className="breakdown-row">
                      <div className="breakdown-label">
                        <span><span className="breakdown-dot movie-dot" />Movies</span>
                        <strong>{watchedMovieCount}</strong>
                      </div>
                      <div className="breakdown-track">
                        <div
                          className="breakdown-fill movie-fill"
                          style={{ width: `${totalActivities ? (watchedMovieCount / totalActivities) * 100 : 0}%` }}
                        />
                      </div>
                    </div>

                    <div className="breakdown-row">
                      <div className="breakdown-label">
                        <span><span className="breakdown-dot tv-dot" />TV episodes</span>
                        <strong>{watchedTvCount}</strong>
                      </div>
                      <div className="breakdown-track">
                        <div
                          className="breakdown-fill tv-fill"
                          style={{ width: `${totalActivities ? (watchedTvCount / totalActivities) * 100 : 0}%` }}
                        />
                      </div>
                    </div>

                    {watchedTitles.length === 0 && (
                      <p className="statistics-note">Your breakdown will update as you track movies and TV shows.</p>
                    )}
                  </section>
                </div>

                <section className="statistics-tip">
                  <span className="stat-icon purple">{"\u2726"}</span>
                  <div>
                    <h3>Keep building your story</h3>
                    <p>Mark titles as watched to grow your personal viewing history and unlock more insights.</p>
                  </div>
                  <button className="secondary-button" onClick={() => navigateToPage("Viewing History")}>
                    View history
                  </button>
                </section>
              </div>
            ) : activePage === "Viewing History" ? (
              backendHistory.length > 0 ? (
                resolvedBackendHistory.length === 0 ? (
                  <div className="empty-state">
                    <h3>Loading viewing history</h3>
                    <p>Loading movie and TV-series details...</p>
                  </div>
                ) : (
                  <div className="movie-grid history-grid">
                    {[...resolvedBackendHistory]
                      .sort(
                        (a, b) =>
                          new Date(b.record.watchedAt).getTime() -
                          new Date(a.record.watchedAt).getTime(),
                      )
                      .map(({ record, title }, index) => (
                        <div
                          className="history-entry"
                          key={`backend-${record.tmdbId}-${record.watchedAt}-${index}`}
                        >
                          <article
                            className="movie-card"
                            role="button"
                            tabIndex={0}
                            onClick={() => {
                              if (record.mediaType === "tv_episode") {
                                setSelectedMovieId(null);
                                setSelectedTvId(record.tmdbId);
                              } else {
                                setSelectedTvId(null);
                                setSelectedMovieId(record.tmdbId);
                              }
                            }}
                            onKeyDown={(event) => {
                              if (event.key === "Enter") {
                                if (record.mediaType === "tv_episode") {
                                  setSelectedMovieId(null);
                                  setSelectedTvId(record.tmdbId);
                                } else {
                                  setSelectedTvId(null);
                                  setSelectedMovieId(record.tmdbId);
                                }
                              }
                            }}
                          >
                            <div className="poster-container">
                              <img
                                src={title.poster}
                                alt={`${title.title} poster`}
                                loading="lazy"
                              />
                              <span className="movie-rating">
                                {"\u2605"} {title.rating}
                              </span>
                            </div>
                            <div className="movie-info">
                              <h3>{title.title}</h3>
                              <div className="movie-meta">
                                <span>{title.year || "Year unavailable"}</span>
                                <span className="meta-dot">{"\u2022"}</span>
                                <span>{title.genre}</span>
                              </div>
                            </div>
                          </article>

                          {record.mediaType === "tv_episode" && (
                            <p className="history-date">
                              Season {record.seasonNumber ?? "?"}, Episode{" "}
                              {record.episodeNumber ?? "?"}
                              {record.runtimeMinutes != null
                                ? ` · ${record.runtimeMinutes} min`
                                : ""}
                            </p>
                          )}

                          {record.mediaType === "movie" &&
                            record.runtimeMinutes != null && (
                              <p className="history-date">
                                Runtime: {record.runtimeMinutes} min
                              </p>
                            )}

                          <p className="history-date">
                            Watched on{" "}
                            {new Date(record.watchedAt).toLocaleString()}
                          </p>
                          <button
                            type="button"
                            className="history-remove-button"
                            disabled={
                              removingHistoryKey ===
                              `${record.tmdbId}-${record.mediaType}-${record.seasonNumber ?? "x"}-${record.episodeNumber ?? "x"}-${record.watchedAt}`
                            }
                            onClick={() =>
                              void removeBackendHistoryRecord(record)
                            }
                          >
                            {removingHistoryKey ===
                            `${record.tmdbId}-${record.mediaType}-${record.seasonNumber ?? "x"}-${record.episodeNumber ?? "x"}-${record.watchedAt}`
                              ? "Removing..."
                              : "Remove from history"}
                          </button>
                        </div>
                      ))}
                  </div>
                )
              ) : (
                watchedTitles.length === 0 ? (
                  <div className="empty-state">
                    <h3>No viewing history yet</h3>
                    <p>Mark a movie or TV show as watched to see it here.</p>
                    <button
                      className="primary-button"
                      onClick={() => navigateToPage("Discover")}
                    >
                      Discover titles
                    </button>
                  </div>
                ) : (
                  <div className="movie-grid history-grid">
                    {[...watchedTitles]
                      .sort(
                        (a, b) =>
                          new Date(b.watchedAt).getTime() -
                          new Date(a.watchedAt).getTime(),
                      )
                      .map((entry) => {
                        const title = (
                          entry.type === "tv" ? allTvCards : allMovieCards
                        ).find((item) => item.id === entry.id);

                        if (!title) return null;

                        return (
                          <div
                            className="history-entry"
                            key={`${entry.type}-${entry.id}`}
                          >
                            <MovieCard
                              movie={title}
                              isSaved={
                                entry.type === "tv"
                                  ? savedTvShows.includes(entry.id)
                                  : savedMovies.includes(entry.id)
                              }
                              isWatched={true}
                              onToggleWatched={() =>
                                toggleWatched(entry.id, entry.type)
                              }
                              onToggleWatchlist={(id) => {
                                if (entry.type === "tv") {
                                  toggleTvWatchlist(id);
                                } else {
                                  toggleWatchlist(id);
                                }
                              }}
                              onOpenDetails={(id) => {
                                if (entry.type === "tv") {
                                  setSelectedMovieId(null);
                                  setSelectedTvId(id);
                                } else {
                                  setSelectedTvId(null);
                                  setSelectedMovieId(id);
                                }
                              }}
                            />
                            <p className="history-date">
                              Watched on{" "}
                              {new Date(entry.watchedAt).toLocaleDateString()}
                            </p>
                            <button
                              className="history-remove-button"
                              onClick={() =>
                                removeWatchedHistory(entry.id, entry.type)
                              }
                            >
                              Remove from history
                            </button>
                          </div>
                        );
                      })}
                  </div>
                )
              )) : displayedMovies.length === 0 ? (
              <div className="empty-state">
                <span className="empty-icon">⌕</span>
                <h3>No movies found</h3>

                <p>
                  {activePage === "My Library"
                    ? "Your watchlist is empty. Discover a film and add it to your library."
                    : "Try another search or choose a different genre."}
                </p>

                {activePage === "My Library" && (
                  <button
                    className="primary-button"
                    onClick={() => setActivePage("Discover")}
                  >
                    Explore movies
                  </button>
                )}
              </div>
            ) : (
              <div className="movie-grid">
                {displayedMovies.map((movie) => {
                  const isTvCard =
                    activePage === "TV Shows" ||
                    movie.genre === "TV Series" ||
                    (activePage === "My Library" &&
                      savedTvShows.includes(movie.id) &&
                      tvShows.some((show) => show.id === movie.id));
                
                  return (
                    <MovieCard
                      key={`${isTvCard ? "tv" : "movie"}-${movie.id}`}
                      movie={movie}
                      isSaved={
                        isTvCard
                          ? savedTvShows.includes(movie.id)
                          : savedMovies.includes(movie.id)
                      }
                      isWatched={
                        isTvCard
                          ? watchedTitles.some(
                              (entry) => entry.id === movie.id && entry.type === "tv",
                            )
                          : backendHistory.some(
                              (record) =>
                                record.tmdbId === movie.id &&
                                record.mediaType === "movie",
                            ) ||
                            watchedTitles.some(
                              (entry) => entry.id === movie.id && entry.type === "movie",
                            )
                      }
                      onToggleWatched={() =>
                        toggleWatched(movie.id, isTvCard ? "tv" : "movie")
                      }
                      onToggleWatchlist={(id) => {
                        if (isTvCard) {
                          toggleTvWatchlist(id);
                        } else {
                          toggleWatchlist(id);
                        }
                      }}
                      onOpenDetails={(id) => {
                        if (isTvCard) {
                          setSelectedMovieId(null);
                          setSelectedTvId(id);
                        } else {
                          setSelectedTvId(null);
                          setSelectedMovieId(id);
                        }
                      }}
                    />
                  );
                })}
              </div>
            )}
          </section>

          {selectedMovieId !== null && (
            <MovieDetails
              movieId={selectedMovieId}
              onClose={() => setSelectedMovieId(null)}
            />
          )}

          {selectedTvId !== null && (
            <TvDetails
              tvId={selectedTvId}
              userId={DASHBOARD_USER_ID}
              history={backendHistory}
              onHistoryChanged={refreshDashboardData}
              onClose={() => setSelectedTvId(null)}
            />
          )}
          <footer className="footer">
            <span>© 2026 CineTrack</span>
            <span>
              MADE FOR THE LOVE OF CINEMA{" "}
              <span className="footer-heart">{"\u2665"}</span>
            </span>
          </footer>
        </div>
      </main>
    </div>
  );
}

export default App;




