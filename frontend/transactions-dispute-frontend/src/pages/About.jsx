import { Link } from "react-router-dom";
import "../About.css";
export default function About() {
  return (
    <div className="about-page">
      {/* NAVBAR */}
      <nav className="navbar">
        <div className="logo">
          TD<span>Bank</span>
        </div>

        <div className="nav-links">
          <Link to="/home">Home</Link>

          <Link to="/about">About Us</Link>
        </div>
      </nav>

      {/* CONTENT */}
      <div className="about-container">
        <div className="about-card">
          <h1>About The Project</h1>

          <p>
            The Transactions Dispute Portal is a
            production-grade banking web application
            built using:
          </p>

          <div className="tech-grid">
            <div className="tech-item">Java</div>

            <div className="tech-item">Spring Boot</div>

            <div className="tech-item">React</div>

            <div className="tech-item">MySQL</div>

            <div className="tech-item">Docker</div>

            <div className="tech-item">REST API</div>
          </div>

          <div className="about-section">
            <h2>Features</h2>

            <ul>
              <li>User registration & login</li>

              <li>View all banking transactions</li>

              <li>Dispute suspicious transactions</li>

              <li>Filter disputed/non-disputed payments</li>

              <li>Responsive banking-style UI</li>

              <li>Secure backend integration</li>
            </ul>
          </div>

          <div className="about-section">
            <h2>Project Goal</h2>

            <p>
              This project demonstrates a modern
              full-stack banking dispute management
              system designed with production-level
              architecture, clean UI/UX, RESTful APIs,
              Docker support, and scalable backend
              services.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
