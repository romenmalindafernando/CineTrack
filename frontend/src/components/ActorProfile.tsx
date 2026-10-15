import { useEffect, useMemo, useState } from "react";
import axios from "axios";
import {
  getActorDetails,
  getActorFilmography,
} from "../services/cinetrackApi";
import type {
  ActorCredit,
  ActorDetails,
} from "../services/cinetrackApi";
import "./ActorProfile.css";

interface ActorProfileProps {
  actorId: number;
  onBack: () => void;
  onOpenTitle: (id: number, mediaType: "movie" | "tv") => void;
}

type MediaFilter = "all" | "movie" | "tv";

const fallbackPoster =
  "https://placehold.co/500x750/17152b/ffffff?text=No+Poster";
const fallbackProfile =
  "https://placehold.co/400x600/17152b/ffffff?text=No+Photo";

function imageUrl(path: string | null, size: "w500" | "w300" = "w500") {
  return path
    ? `https://image.tmdb.org/t/p/${size}${path}`
    : fallbackPoster;
}

function releaseYear(date: string | null) {
  return date ? date.slice(0, 4) : "Year unavailable";
}

function ActorProfile({
  actorId,
  onBack,
  onOpenTitle,
}: ActorProfileProps) {
  const [actor, setActor] = useState<ActorDetails | null>(null);
  const [credits, setCredits] = useState<ActorCredit[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [filter, setFilter] = useState<MediaFilter>("all");
  const [minimumVotes, setMinimumVotes] = useState(500);
  const [sortOrder, setSortOrder] = useState<"rating" | "newest">("rating");

  useEffect(() => {
    let cancelled = false;

    async function loadActorProfile() {
      setLoading(true);
      setError("");
      setActor(null);
      setCredits([]);

      try {
        const [actorResult, creditsResult] = await Promise.all([
          getActorDetails(actorId),
          getActorFilmography(actorId),
        ]);

        if (!cancelled) {
          setActor(actorResult);
          setCredits(creditsResult);
        }
      } catch (err) {
        if (!cancelled) {
          setError(
            axios.isAxiosError(err)
              ? "Unable to load this actor. Check the backend connection and try again."
              : "Something went wrong while loading the actor profile.",
          );
        }
      } finally {
        if (!cancelled) setLoading(false);
      }
    }

    void loadActorProfile();

    return () => {
      cancelled = true;
    };
  }, [actorId]);

  const filteredCredits = useMemo(() => {
    const filtered = credits.filter((credit) => {
      const matchesType =
        filter === "all" || credit.mediaType === filter;
      const matchesVotes = credit.voteCount >= minimumVotes;
      return matchesType && matchesVotes;
    });

    return filtered.sort((a, b) => {
      if (sortOrder === "newest") {
        return (b.releaseDate ?? "").localeCompare(a.releaseDate ?? "");
      }
      return b.rating - a.rating || b.voteCount - a.voteCount;
    });
  }, [credits, filter, minimumVotes, sortOrder]);

  if (loading) {
    return (
      <section className="actor-profile-page">
        <button className="actor-back-button" onClick={onBack}>
          ← Back
        </button>
        <div className="actor-profile-message">Loading actor profile...</div>
      </section>
    );
  }

  if (error || !actor) {
    return (
      <section className="actor-profile-page">
        <button className="actor-back-button" onClick={onBack}>
          ← Back
        </button>
        <div className="actor-profile-message">
          <h2>Actor profile unavailable</h2>
          <p>{error || "Actor information could not be found."}</p>
          <button className="actor-filter-button" onClick={() => window.location.reload()}>
            Retry
          </button>
        </div>
      </section>
    );
  }

  const profileImage = actor.profilePath
    ? `https://image.tmdb.org/t/p/w500${actor.profilePath}`
    : fallbackProfile;

  return (
    <section className="actor-profile-page">
      <button className="actor-back-button" onClick={onBack}>
        ← Back
      </button>

      <header className="actor-hero">
        <img
          className="actor-profile-photo"
          src={profileImage}
          alt={actor.name}
        />
        <div className="actor-profile-info">
          <p className="actor-eyebrow">
            {actor.knownForDepartment || "Cast and crew"}
          </p>
          <h1>{actor.name}</h1>
          {actor.birthday && (
            <p>
              <strong>Born:</strong> {actor.birthday}
            </p>
          )}
          {actor.placeOfBirth && (
            <p>
              <strong>Place of birth:</strong> {actor.placeOfBirth}
            </p>
          )}
          {actor.biography && (
            <div className="actor-biography">
              <h2>Biography</h2>
              <p>{actor.biography}</p>
            </div>
          )}
        </div>
      </header>

      <div className="actor-filmography-heading">
        <div>
          <p className="actor-eyebrow">CAREER OVERVIEW</p>
          <h2>Filmography</h2>
          <p>{filteredCredits.length} titles match your filters</p>
        </div>
      </div>

      <div className="actor-filmography-controls">
        <div className="actor-filter-group" aria-label="Filter by media type">
          {([
            ["all", "All"],
            ["movie", "Movies"],
            ["tv", "TV Shows"],
          ] as const).map(([value, label]) => (
            <button
              key={value}
              className={`actor-filter-button ${filter === value ? "active" : ""}`}
              onClick={() => setFilter(value)}
              aria-pressed={filter === value}
            >
              {label}
            </button>
          ))}
        </div>

        <label className="actor-select-label">
          Minimum TMDB votes
          <select
            value={minimumVotes}
            onChange={(event) => setMinimumVotes(Number(event.target.value))}
          >
            <option value={0}>All vote counts</option>
            <option value={100}>100+</option>
            <option value={500}>500+</option>
            <option value={1000}>1,000+</option>
            <option value={5000}>5,000+</option>
          </select>
        </label>

        <label className="actor-select-label">
          Sort by
          <select
            value={sortOrder}
            onChange={(event) =>
              setSortOrder(event.target.value as "rating" | "newest")
            }
          >
            <option value="rating">Highest TMDB rating</option>
            <option value="newest">Newest first</option>
          </select>
        </label>
      </div>

      {filteredCredits.length === 0 ? (
        <div className="actor-profile-message">
          No titles match these filters. Try lowering the minimum vote count.
        </div>
      ) : (
        <div className="actor-filmography-grid">
          {filteredCredits.map((credit, index) => (
            <button
              className="actor-credit-card"
              key={`${credit.mediaType}-${credit.id}-${index}`}
              onClick={() => onOpenTitle(credit.id, credit.mediaType)}
              aria-label={`Open ${credit.title} details`}
            >
              <div className="actor-credit-poster-wrap">
                <img
                  className="actor-credit-poster"
                  src={imageUrl(credit.posterPath)}
                  alt={`${credit.title} poster`}
                  loading="lazy"
                />
                <span className="actor-credit-rating">
                  ★ {Number(credit.rating || 0).toFixed(1)}
                </span>
              </div>
              <div className="actor-credit-info">
                <span className="actor-credit-type">
                  {credit.mediaType === "tv" ? "TV SHOW" : "MOVIE"}
                </span>
                <h3>{credit.title}</h3>
                <p>{releaseYear(credit.releaseDate)}</p>
                {credit.character && (
                  <p className="actor-credit-character">
                    as {credit.character}
                  </p>
                )}
                <p className="actor-credit-votes">
                  {credit.voteCount.toLocaleString()} TMDB votes
                </p>
              </div>
            </button>
          ))}
        </div>
      )}
    </section>
  );
}

export default ActorProfile;