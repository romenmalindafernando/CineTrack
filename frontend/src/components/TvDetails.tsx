import { useEffect, useState } from "react";
import axios from "axios";
import {
  getTvDetails,
  getEpisodeDetails,
  addWatchedEpisode,
  removeWatchedEpisode,
} from "../services/cinetrackApi";
import type {
  TvDetails as TvDetailsData,
  TvEpisodeDetails,
  ViewingHistoryRecord,
} from "../services/cinetrackApi";
import "./MovieDetails.css";

interface TvDetailsProps {
  tvId: number;
  userId: string;
  history: ViewingHistoryRecord[];
  onHistoryChanged: () => Promise<void>;
  onClose: () => void;
}

function TvDetails({
  tvId,
  userId,
  history,
  onHistoryChanged,
  onClose,
}: TvDetailsProps) {
  const [show, setShow] = useState<TvDetailsData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
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

  return (
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
          <div className="details-message">
            <h2>Loading TV show details...</h2>
            <p>Getting the latest information from CineTrack.</p>
          </div>
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
                backgroundImage: `linear-gradient(90deg, #111020 5%, rgba(17,16,32,.82) 55%, rgba(17,16,32,.35)), url("${imageUrl(show.backdropPath, "w1280")}")`,
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
  );
}

export default TvDetails;