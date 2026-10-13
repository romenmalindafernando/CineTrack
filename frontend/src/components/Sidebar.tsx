interface SidebarProps {
  activePage: string;
  savedCount: number;
  historyCount: number;
  onNavigate: (page: string) => void;
}

const navigation = [
  { label: "Discover", icon: "⌂" },
  { label: "Movies", icon: "▣" },
  { label: "TV Shows", icon: "▤" },
];

function Sidebar({
  activePage,
  savedCount,
  historyCount,
  onNavigate,
}: SidebarProps) {
  return (
    <aside className="sidebar">
      <a
        className="brand"
        href="#"
        onClick={(event) => {
          event.preventDefault();
          onNavigate("Discover");
        }}
      >
        <span className="brand-icon">C</span>
        <span>
          <strong>CineTrack</strong>
          <small>YOUR MOVIE UNIVERSE</small>
        </span>
      </a>

      <div className="sidebar-section">
        <p className="sidebar-heading">MENU</p>

        {navigation.map((item) => (
          <button
            key={item.label}
            className={`nav-item ${
              activePage === item.label ? "active" : ""
            }`}
            onClick={() => onNavigate(item.label)}
          >
            <span className="nav-icon">{item.icon}</span>
            {item.label}
          </button>
        ))}
      </div>

      <div className="sidebar-section">
        <p className="sidebar-heading">YOUR SPACE</p>

        <button
          className={`nav-item ${
            activePage === "My Library" ? "active" : ""
          }`}
          onClick={() => onNavigate("My Library")}
        >
          <span className="nav-icon">♡</span>
          My Library
          <span className="nav-count">{savedCount}</span>
        </button>

        <button
          className={`nav-item ${
            activePage === "Statistics" ? "active" : ""
          }`}
          onClick={() => onNavigate("Statistics")}
        >
          <span className="nav-icon">▥</span>
          Statistics
        </button>

        <button
          className={`nav-item ${activePage === "Viewing History" ? "active" : ""}`}
          onClick={() => onNavigate("Viewing History")}
        >
          <span className="nav-icon">{"\u2713"}</span>
          Viewing History
          <span className="nav-count">{historyCount}</span>
        </button>
      </div>

      <div className="sidebar-bottom">
        <div className="profile-avatar">R</div>

        <div className="profile-info">
          <strong>Movie Enthusiast</strong>
          <small>Your personal space</small>
        </div>

        <span className="profile-menu">···</span>
      </div>
    </aside>
  );
}

export default Sidebar;