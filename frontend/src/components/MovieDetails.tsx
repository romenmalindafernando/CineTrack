import { useEffect, useState } from "react";
import axios from "axios";
import { getMovieDetails } from "../services/cinetrackApi";
import type { MovieDetails as MovieDetailsData } from "../services/cinetrackApi";
import "./MovieDetails.css";

interface MovieDetailsProps {
  movieId: number;
  onClose: () => void;
}

function MovieDetails({ movieId, onClose }: MovieDetailsProps) {
  const [movie, setMovie] = useState<MovieDetailsData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;

    async function loadDetails() {
      setLoading(true);
      setError("");
      setMovie(null);

      try {
        const result = await getMovieDetails(movieId);
        if (!cancelled) setMovie(result);
      } catch (err) {
        if (!cancelled) {
          setError(
            axios.isAxiosError(err)
              ? "Unable to load movie details. Check that the backend is running."
              : "Something went wrong while loading this movie.",
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
  }, [movieId]);

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

  return (
    <div className="details-overlay" onClick={onClose}>
      <section
        className="details-panel"
        role="dialog"
        aria-modal="true"
        aria-label="Movie details"
        onClick={(event) => event.stopPropagation()}
      >
        <button
          type="button"
          className="details-close"
          onClick={onClose}
          aria-label="Close movie details"
        >
          Close
        </button>

        {loading ? (
          <div className="details-message">
            <h2>Loading movie details...</h2>
            <p>Getting the latest information from CineTrack.</p>
          </div>
        ) : error ? (
          <div className="details-message">
            <h2>Details unavailable</h2>
            <p>{error}</p>
          </div>
        ) : movie ? (
          <>
            <div
              className="details-backdrop"
              style={{
                backgroundImage: `linear-gradient(90deg, #111020 5%, rgba(17,16,32,.82) 55%, rgba(17,16,32,.35)), url("${imageUrl(movie.backdropPath, "w1280")}")`,
              }}
            />

            <div className="details-content">
              <img
                className="details-poster"
                src={imageUrl(movie.posterPath, "w500")}
                alt={`${movie.title} poster`}
              />

              <div className="details-information">
                <p className="details-eyebrow">CINETRACK MOVIE DETAILS</p>
                <h2>{movie.title}</h2>

                <div className="details-meta">
                  {movie.releaseDate?.slice(0, 4) || "Release year unknown"}
                  <span>•</span>
                  {movie.runtime > 0
                    ? `${Math.floor(movie.runtime / 60)}h ${movie.runtime % 60}m`
                    : "Runtime unavailable"}
                  <span>•</span>
                  <span>Rating: {movie.rating.toFixed(1)}/10</span>
                </div>

                <div className="details-genres">
                  {movie.genres?.map((genre) => (
                    <span className="details-genre" key={genre}>
                      {genre}
                    </span>
                  ))}
                </div>

                <h3>Overview</h3>
                <p className="details-overview">
                  {movie.overview || "No overview is available for this movie."}
                </p>
              </div>
            </div>
          </>
        ) : null}
      </section>
    </div>
  );
}

export default MovieDetails;
