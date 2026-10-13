export interface Movie {
  id: number;
  title: string;
  year: number;
  rating: number;
  genre: string;
  poster: string;
}

export const movies: Movie[] = [
  {
    id: 157336,
    title: "Interstellar",
    year: 2014,
    rating: 8.7,
    genre: "Sci-Fi",
    poster:
      "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
  },
  {
    id: 693134,
    title: "Dune: Part Two",
    year: 2024,
    rating: 8.5,
    genre: "Adventure",
    poster:
      "https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
  },
  {
    id: 414906,
    title: "The Batman",
    year: 2022,
    rating: 7.8,
    genre: "Action",
    poster:
      "https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg",
  },
  {
    id: 872585,
    title: "Oppenheimer",
    year: 2023,
    rating: 8.3,
    genre: "Drama",
    poster:
      "https://image.tmdb.org/t/p/w500/ptpr0kGAckfQkJeJIt8st5dglvd.jpg",
  },
  {
    id: 569094,
    title: "Spider-Man: Across the Spider-Verse",
    year: 2023,
    rating: 8.6,
    genre: "Animation",
    poster:
      "https://image.tmdb.org/t/p/w500/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg",
  },
  {
    id: 27205,
    title: "Inception",
    year: 2010,
    rating: 8.8,
    genre: "Sci-Fi",
    poster:
      "https://image.tmdb.org/t/p/w500/oYuLEt3zVCKq57qu2F8dT7NIa6f.jpg",
  },
];
