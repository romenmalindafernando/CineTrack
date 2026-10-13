interface TopbarProps {
  activePage: string;
  search: string;
  onSearchChange: (value: string) => void;
}

function Topbar({
  activePage,
  search,
  onSearchChange,
}: TopbarProps) {
  return (
    <header className="topbar">
      <div className="breadcrumb">
        <span>Home</span>
        <span className="breadcrumb-divider">/</span>
        <strong>{activePage}</strong>
      </div>

      <label className="search-box">
        <span>⌕</span>

        <input
          type="search"
          placeholder="Search movies..."
          value={search}
          onChange={(event) => onSearchChange(event.target.value)}
        />

        <kbd>/</kbd>
      </label>

      <button
        className="notification-button"
        aria-label="Notifications"
        type="button"
      >
        ♧
      </button>
    </header>
  );
}

export default Topbar;