import type { Movie } from "../data/movies";

interface MovieCardProps {
  movie: Movie;
  isSaved: boolean;
  isWatched: boolean;
  onToggleWatched: () => void;
  onToggleWatchlist: (id: number) => void;
  onOpenDetails: (id: number) => void;
}

function MovieCard({
  movie,
  isSaved,
  isWatched,
  onToggleWatched,
  onToggleWatchlist,
  onOpenDetails,
}: MovieCardProps) {
  return (
    <article className="movie-card" onClick={() => onOpenDetails(movie.id)} role="button" tabIndex={0} onKeyDown={(event) => { if (event.key === "Enter") onOpenDetails(movie.id); }}>
      <div className="poster-container">
        <img
          src={movie.poster}
          alt={`${movie.title} poster`}
          loading="lazy"
        />

        <span className="movie-rating">{"\u2605"} {movie.rating}</span>

        <button
          className={`watchlist-button ${isSaved ? "saved" : ""}`}
          aria-label={
            isSaved
              ? `Remove ${movie.title} from watchlist`
              : `Add ${movie.title} to watchlist`
          }
          onClick={(event) => { event.stopPropagation(); onToggleWatchlist(movie.id); }}
        >
          {isSaved ? "\u2665" : "+"}
        </button>
        <button
          type="button"
          className={`watched-button ${isWatched ? "watched" : ""}`}
          title={isWatched ? "Mark as unwatched" : "Mark as watched"}
          aria-label={isWatched ? `Mark ${movie.title} as unwatched` : `Mark ${movie.title} as watched`}
          onClick={(event) => {
            event.stopPropagation();
            onToggleWatched();
          }}
        >
          {isWatched ? "\u2713" : "\u25CB"}
        </button>
      </div>

      <div className="movie-info">
        <h3>{movie.title}</h3>

        <div className="movie-meta">
          <span>{movie.year}</span>
          <span className="meta-dot">{"\u2022"}</span>
          <span>{movie.genre}</span>
        </div>
      </div>
    </article>
  );
}

export default MovieCard;
