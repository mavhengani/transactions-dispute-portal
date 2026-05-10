import { Link } from "react-router-dom";

export default function Navbar() {

  const logout = () => {

    localStorage.clear();

    window.location.href = "/";
  };

  return (

    <nav className="navbar">

      <div className="logo">
        TD<span>Bank</span>
      </div>

      <div className="nav-links">

        <Link to="/home">
          Home
        </Link>

        <Link to="/about">
          About
        </Link>

        <button
          className="logout-btn"
          onClick={logout}
        >
          Logout
        </button>

      </div>

    </nav>
  );
}