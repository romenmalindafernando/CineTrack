import axios from "axios";
import type { Movie } from "../data/movies";

const api = axios.create({
  baseURL: "/api",
  headers: {
    Accept: "application/json",
  },
});

interface MovieSearchItem {
  id: number;
  title: string;
  releaseDate?: string | null;
  rating?: number | null;
  posterPath?: string | null;
}

interface TvSearchItem {
  id: number;
  name: string;
  firstAirDate?: string | null;
  rating?: number | null;
  posterPath?: string | null;
}

interface SearchResponse<T> {
  page: number;
  totalPages: number;
  totalResults: number;
  results: T[];
}

const fallbackPoster =
  "https://placehold.co/500x750/17152b/ffffff?text=No+Poster";

function toYear(date?: string | null): number {
  if (!date) return 0;

  const year = Number(date.slice(0, 4));
  return Number.isFinite(year) ? year : 0;
}

function toPoster(path?: string | null): string {
  return path
    ? `https://image.tmdb.org/t/p/w500${path}`
    : fallbackPoster;
}

export async function searchMovies(query: string): Promise<Movie[]> {
  const response = await api.get<SearchResponse<MovieSearchItem>>(
    "/movies/search",
    { params: { query } },
  );

  return response.data.results.map((item) => ({
    id: item.id,
    title: item.title,
    year: toYear(item.releaseDate),
    rating: item.rating ?? 0,
    genre: "Movie",
    poster: toPoster(item.posterPath),
  }));
}

export async function searchTvShows(query: string): Promise<Movie[]> {
  const response = await api.get<SearchResponse<TvSearchItem>>(
    "/tv/search",
    { params: { query } },
  );

  return response.data.results.map((item) => ({
    id: item.id,
    title: item.name,
    year: toYear(item.firstAirDate),
    rating: item.rating ?? 0,
    genre: "TV Series",
    poster: toPoster(item.posterPath),
  }));
}

export interface MovieDetails {
  id: number;
  title: string;
  releaseDate: string | null;
  rating: number;
  runtime: number;
  posterPath: string | null;
  backdropPath: string | null;
  overview: string;
  genres: string[];
}

export async function getMovieDetails(
  movieId: number,
): Promise<MovieDetails> {
  const response = await api.get<MovieDetails>(`/movies/${movieId}`);
  return response.data;
}

export interface TvDetails {
  id: number;
  name: string;
  firstAirDate: string | null;
  rating: number;
  numberOfSeasons: number;
  numberOfEpisodes: number;
  posterPath: string | null;
  backdropPath: string | null;
  overview: string;
  genres: string[];
}

export async function getTvDetails(tvId: number): Promise<TvDetails> {
  const response = await api.get<TvDetails>(`/tv/${tvId}`);
  return response.data;
}

export interface UserStats {
  moviesWatched: number;
  tvSeriesWatched: number;
  episodesWatched: number;
  moviesInWatchlist: number;
  tvSeriesInWatchlist: number;
  totalReviews: number;
  averageRating: number;
}

export interface MonthlyStats {
  month: string;
  moviesWatched: number;
  tvSeriesWatched: number;
  episodesWatched: number;
  totalActivities: number;
  totalViewingMinutes: number;
  totalViewingHours: number;
}

export interface YearlyStats {
  year: number;
  moviesWatched: number;
  tvSeriesWatched: number;
  episodesWatched: number;
  totalActivities: number;
  totalViewingMinutes: number;
  totalViewingHours: number;
  monthlyBreakdown: MonthlyStats[];
}

export interface ViewingHistoryRecord {
  tmdbId: number;
  mediaType: "movie" | "tv" | "tv_episode";
  runtimeMinutes: number | null;
  watchedAt: string;
  seasonNumber: number | null;
  episodeNumber: number | null;
}

export async function getUserStats(userId: string): Promise<UserStats> {
  const response = await api.get<UserStats>(`/users/${userId}/stats`);
  return response.data;
}

export async function getViewingHistory(
  userId: string,
): Promise<ViewingHistoryRecord[]> {
  const response = await api.get<ViewingHistoryRecord[]>(
    `/users/${userId}/history`,
  );
  return response.data;
}

export async function getMonthlyStats(
  userId: string,
): Promise<MonthlyStats[]> {
  const response = await api.get<MonthlyStats[]>(
    `/users/${userId}/stats/monthly`,
  );
  return response.data;
}

export async function getYearlyStats(
  userId: string,
  year: number,
): Promise<YearlyStats> {
  const response = await api.get<YearlyStats>(
    `/users/${userId}/stats/yearly`,
    { params: { year } },
  );
  return response.data;
}

export async function addToWatched(
  userId: string,
  tmdbId: number,
  mediaType: "movie" | "tv",
): Promise<void> {
  await api.post(`/users/${userId}/watched/${tmdbId}`, null, {
    params: { mediaType },
  });
}

export async function removeFromWatched(
  userId: string,
  tmdbId: number,
  mediaType: "movie" | "tv",
): Promise<void> {
  await api.delete(`/users/${userId}/watched/${tmdbId}`, {
    params: { mediaType },
  });
}

export interface TvEpisodeDetails {
  id: number;
  name: string;
  overview: string;
  seasonNumber: number;
  episodeNumber: number;
  airDate: string | null;
  stillPath: string | null;
  rating: number;
  runtimeMinutes: number | null;
}

export async function getEpisodeDetails(
  tvId: number,
  seasonNumber: number,
  episodeNumber: number,
): Promise<TvEpisodeDetails> {
  const response = await api.get<TvEpisodeDetails>(
    `/tv/${tvId}/season/${seasonNumber}/episode/${episodeNumber}`,
  );
  return response.data;
}

export async function addWatchedEpisode(
  userId: string,
  tvId: number,
  seasonNumber: number,
  episodeNumber: number,
): Promise<void> {
  await api.post(
    `/users/${userId}/tv/${tvId}/seasons/${seasonNumber}/episodes/${episodeNumber}/watched`,
  );
}

export async function removeWatchedEpisode(
  userId: string,
  tvId: number,
  seasonNumber: number,
  episodeNumber: number,
): Promise<void> {
  await api.delete(
    `/users/${userId}/tv/${tvId}/seasons/${seasonNumber}/episodes/${episodeNumber}/watched`,
  );
}