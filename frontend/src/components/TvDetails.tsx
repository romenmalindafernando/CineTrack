import { createPortal } from "react-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import {
  getTvDetails,
  getTvCast,
  getEpisodeDetails,
  addWatchedEpisode,
  removeWatchedEpisode,
} from "../services/cinetrackApi";
import type {
  TvDetails as TvDetailsData,
  CastMember,
  TvEpisodeDetails,
  ViewingHistoryRecord,
} from "../services/cinetrackApi";
import "./MovieDetails.css";

interface TvDetailsProps {
  tvId: number;
  userId: string;
  history: ViewingHistoryRecord[];
  onHistoryChanged: () => Promise<void>;
  onActorClick: (actorId: number) => void;
  onClose: () => void;
}

function TvDetails({
  tvId,
  userId,
  history,
  onHistoryChanged,
  onActorClick,
  onClose,
}: TvDetailsProps) {
  const [show, setShow] = useState<TvDetailsData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [cast, setCast] = useState<CastMember[]>([]);
  const [castLoading, setCastLoading] = useState(true);
  const [seasonNumber, setSeasonNumber] = useState(1);
  const [episodeNumber, setEpisodeNumber] = useState(1);
  const [episode, setEpisode] = useState<TvEpisodeDetails | null>(null);
  const [episodeLoading, setEpisodeLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [episodeError, setEpisodeError] = useState("");
  const [episodeMessage, setEpisodeMessage] = useState("");

  useEffect(() => {
    let cancelled = false;

    async function loadDetails() {
      setLoading(true);
      setError("");
      setShow(null);
      setEpisode(null);

      try {
        const result = await getTvDetails(tvId);
        if (!cancelled) {
          setShow(result);
          setSeasonNumber(1);
          setEpisodeNumber(1);
        }
      } catch (err) {
        if (!cancelled) {
          setError(
            axios.isAxiosError(err)
              ? "Unable to load TV show details. Check that the backend is running."
              : "Something went wrong while loading this TV show.",
          );
        }
      } finally {
        if (!cancelled) setLoading(false);
      }
    }

    void loadDetails();
    return () => {
      cancelled = true;
    };
  }, [tvId]);

  useEffect(() => {
    let cancelled = false;

    async function loadCast() {
      setCast([]);
      setCastLoading(true);

      try {
        const result = await getTvCast(tvId);
        if (!cancelled) setCast(result);
      } catch {
        if (!cancelled) setCast([]);
      } finally {
        if (!cancelled) setCastLoading(false);
      }
    }

    void loadCast();

    return () => {
      cancelled = true;
    };
  }, [tvId]);

  useEffect(() => {
    function handleKeyDown(event: KeyboardEvent) {
      if (event.key === "Escape") onClose();
    }

    window.addEventListener("keydown", handleKeyDown);
    return () => window.removeEventListener("keydown", handleKeyDown);
  }, [onClose]);

  const imageUrl = (path: string | null, size: string) =>
    path
      ? `https://image.tmdb.org/t/p/${size}${path}`
      : "https://placehold.co/500x750/17152b/ffffff?text=No+Poster";

  const isEpisodeWatched = history.some(
    (record) =>
      record.tmdbId === tvId &&
      record.mediaType === "tv_episode" &&
      record.seasonNumber === seasonNumber &&
      record.episodeNumber === episodeNumber,
  );

  async function loadEpisode() {
    setEpisodeLoading(true);
    setEpisodeError("");
    setEpisodeMessage("");
    setEpisode(null);

    try {
      const result = await getEpisodeDetails(
        tvId,
        seasonNumber,
        episodeNumber,
      );
      setEpisode(result);
    } catch (err) {
      setEpisodeError(
        axios.isAxiosError(err)
          ? "Could not load this episode. Check the season and episode numbers."
          : "Something went wrong while loading the episode.",
      );
    } finally {
      setEpisodeLoading(false);
    }
  }

  async function toggleEpisodeWatched() {
    setSaving(true);
    setEpisodeError("");
    setEpisodeMessage("");

    try {
      if (isEpisodeWatched) {
        await removeWatchedEpisode(
          userId,
          tvId,
          seasonNumber,
          episodeNumber,
        );
        setEpisodeMessage("Episode removed from your viewing history.");
      } else {
        await addWatchedEpisode(
          userId,
          tvId,
          seasonNumber,
          episodeNumber,
        );
        setEpisodeMessage("Episode added to your viewing history.");
      }

      await onHistoryChanged();
    } catch {
      setEpisodeError(
        "Could not update your viewing history. Please check that the backend is running and try again.",
      );
    } finally {
      setSaving(false);
    }
  }

// Lock background page scrolling while details are open.
  useEffect(() => {
    const previousBodyOverflow = document.body.style.overflow;
    const previousHtmlOverflow = document.documentElement.style.overflow;

    document.body.style.overflow = "hidden";
    document.documentElement.style.overflow = "hidden";

    return () => {
      document.body.style.overflow = previousBodyOverflow;
      document.documentElement.style.overflow = previousHtmlOverflow;
    };
  }, []);
  return createPortal((
    <div className="details-overlay" onClick={onClose}>
      <section
        className="details-panel"
        role="dialog"
        aria-modal="true"
        aria-label="TV show details"
        onClick={(event) => event.stopPropagation()}
      >
        <button
          type="button"
          className="details-close" onClick={(event) => { event.stopPropagation(); onClose(); }}
          aria-label="Close TV show details"
        >
          Close
        </button>

        {loading ? (
          <div
            className="details-loading"
            role="status"
            aria-label="Loading TV show details"
            aria-live="polite"
          />
        ) : error ? (
          <div className="details-message">
            <h2>Details unavailable</h2>
            <p>{error}</p>
          </div>
        ) : show ? (
          <>
            <div
              className="details-backdrop"
              style={{
                backgroundImage: `linear-gradient(180deg, rgba(12, 10, 24, 0.12) 0%, rgba(12, 10, 24, 0.28) 48%, rgba(12, 10, 24, 0.88) 100%), url("${imageUrl((show as typeof show & { backdropPath?: string | null }).backdropPath || show.posterPath, "w1280")}")`,
              }}
            />

            <div className="details-content">
              <img
                className="details-poster"
                src={imageUrl(show.posterPath, "w500")}
                alt={`${show.name} poster`}
              />

              <div className="details-information">
                <p className="details-eyebrow">CINETRACK TV SERIES DETAILS</p>
                <h2>{show.name}</h2>

                <div className="details-meta">
                  {show.firstAirDate?.slice(0, 4) || "First air year unknown"}
                  <span>•</span>
                  <span>
                    {show.numberOfSeasons}{" "}
                    {show.numberOfSeasons === 1 ? "Season" : "Seasons"}
                  </span>
                  <span>•</span>
                  <span>
                    {show.numberOfEpisodes}{" "}
                    {show.numberOfEpisodes === 1 ? "Episode" : "Episodes"}
                  </span>
                  <span>•</span>
                  <span>Rating: {show.rating.toFixed(1)}/10</span>
                </div>

                <div className="details-genres">
                  {show.genres?.map((genre) => (
                    <span className="details-genre" key={genre}>
                      {genre}
                    </span>
                  ))}
                </div>

                <h3>Overview</h3>
                <p className="details-overview">
                  {show.overview ||
                    "No overview is available for this TV show."}
                </p>

                <section className="cast-section" aria-label="Cast and characters">
                  <h3>Cast &amp; Characters</h3>
                  {castLoading ? (
                    <p className="cast-message">Loading cast...</p>
                  ) : cast.length > 0 ? (
                    <div className="cast-list">
                      {cast.map((member) => (
                        <article className="cast-card" key={member.id}>
                          <button
                            type="button"
                            className="cast-actor-button"
                            onClick={() => onActorClick(member.id)}
                            aria-label={`View ${member.name}'s actor profile`}
                          >
                          <img
                            className="cast-photo"
                            src={
                              member.profilePath
                                ? `https://image.tmdb.org/t/p/w185${member.profilePath}`
                                : "https://placehold.co/185x278/17152b/ffffff?text=No+Photo"
                            }
                            alt={`${member.name} as ${member.character}`}
                            loading="lazy"
                          />
                          <p className="cast-name">{member.name}</p>
                          </button>
                          <p className="cast-character">
                            {member.character || "Character unavailable"}
                          </p>
                        </article>
                      ))}
                    </div>
                  ) : (
                    <p className="cast-message">Cast information is currently unavailable.</p>
                  )}
                </section>

                <div className="episode-tracker">
                  <h3>Track a TV Episode</h3>
                  <p>
                    Select a season and episode to load its details and update
                    your viewing history.
                  </p>

                  <div className="episode-selector">
                    <label>
                      Season
                      <select
                        value={seasonNumber}
                        onChange={(event) => {
                          setSeasonNumber(Number(event.target.value));
                          setEpisode(null);
                          setEpisodeError("");
                          setEpisodeMessage("");
                        }}
                      >
                        {Array.from(
                          { length: show.numberOfSeasons },
                          (_, index) => index + 1,
                        ).map((season) => (
                          <option key={season} value={season}>
                            Season {season}
                          </option>
                        ))}
                      </select>
                    </label>

                    <label>
                      Episode number
                      <input
                        type="number"
                        min={1}
                        max={100}
                        value={episodeNumber}
                        onChange={(event) => {
                          setEpisodeNumber(
                            Math.max(1, Number(event.target.value) || 1),
                          );
                          setEpisode(null);
                          setEpisodeError("");
                          setEpisodeMessage("");
                        }}
                      />
                    </label>

                    <button
                      type="button"
                      onClick={() => void loadEpisode()}
                      disabled={episodeLoading}
                    >
                      {episodeLoading ? "Loading episode..." : "Load episode"}
                    </button>
                  </div>

                  {episodeError && (
                    <p className="episode-feedback episode-error">
                      {episodeError}
                    </p>
                  )}

                  {episodeMessage && (
                    <p className="episode-feedback episode-success">
                      {episodeMessage}
                    </p>
                  )}

                  {episode && (
                    <div className="episode-result">
                      {episode.stillPath && (
                        <img
                          className="episode-still"
                          src={imageUrl(episode.stillPath, "w500")}
                          alt={`${episode.name} still`}
                        />
                      )}

                      <h4>
                        S{String(episode.seasonNumber).padStart(2, "0")}:
                        E{String(episode.episodeNumber).padStart(2, "0")} —{" "}
                        {episode.name || "Untitled episode"}
                      </h4>

                      <p className="details-meta">
                        {episode.airDate || "Air date unavailable"}
                        {" • "}
                        {episode.runtimeMinutes != null
                          ? `${episode.runtimeMinutes} minutes`
                          : "Runtime unavailable"}
                        {" • "}
                        Rating: {Number(episode.rating || 0).toFixed(1)}/10
                      </p>

                      <p className="details-overview">
                        {episode.overview || "No episode overview available."}
                      </p>

                      <button
                        type="button"
                        className="episode-watch-button"
                        onClick={() => void toggleEpisodeWatched()}
                        disabled={saving}
                      >
                        {saving
                          ? "Saving..."
                          : isEpisodeWatched
                            ? "Remove from watched history"
                            : "Mark episode as watched"}
                      </button>
                    </div>
                  )}
                </div>
              </div>
            </div>
          </>
        ) : null}
      </section>
    </div>
  ), document.body);
}

export default TvDetails;