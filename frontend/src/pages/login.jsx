import { useState } from "react";

//Left side brand panel component
function BrandPanel() {
  return (
    <aside className="brand-panel">
      <div className="brand-content">
        <div className="brand-mark" aria-hidden="true">
          🛡️
        </div>
        <h1>
          Study together.
          <br />
          Level up together.
        </h1>
        <p>
          Tambayan Guild turns group study into a team campaign with shared
          decks, live trivia, and daily quests.
        </p>
        <ul className="feature-list">
          <li>
            <span aria-hidden="true">👥</span> Shared flashcard decks
          </li>
          <li>
            <span aria-hidden="true">⚡</span> Real-time multiplayer trivia
          </li>
          <li>
            <span aria-hidden="true">🔥</span> Daily quests, XP &amp;
            leaderboards
          </li>
        </ul>
      </div>
    </aside>
  );
}

//Right side login form component
export default function LoginPage({ onSwitchToSignUp }) {
  const [form, setForm] = useState({ email: "", password: "" });
  const [submitting, setSubmitting] = useState(false);
  const [message, setMessage] = useState("");

  function handleChange(event) {
    setForm((current) => ({
      ...current,
      [event.target.name]: event.target.value,
    }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setMessage("");
    setSubmitting(true);
    try {
      const response = await fetch("/api/auth/login", {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });
      const data = await response.json().catch(() => ({}));
      if (!response.ok) {
        setMessage(
          data.message || "Could not log in. Check your details and try again.",
        );
        return;
      }
      localStorage.setItem("tambayanGuildUser", JSON.stringify(data));
      setMessage("You’re logged in!");
    } catch {
      setMessage("Unable to reach server on port 8081.");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="auth-layout">
      <BrandPanel />
      <main className="form-panel">
        <div className="auth-form-wrap">
          <header className="form-heading">
            <h2>Welcome back</h2>
            <p>Log in to keep your streak alive.</p>
          </header>
          <form onSubmit={handleSubmit}>
            <div className="form-field">
              <label htmlFor="login-email">School email</label>
              <input
                id="login-email"
                name="email"
                type="email"
                autoComplete="email"
                required
                placeholder="you@email.com"
                value={form.email}
                onChange={handleChange}
              />
            </div>
            <div className="form-field">
              <label htmlFor="login-password">Password</label>
              <input
                id="login-password"
                name="password"
                type="password"
                autoComplete="current-password"
                required
                placeholder="Enter your password"
                value={form.password}
                onChange={handleChange}
              />
            </div>
            <div className="forgot-row">
              <button
                type="button"
                onClick={() => setMessage("Password reset is not set up yet.")}
              >
                Forgot password?
              </button>
            </div>
            {message && (
              <p className="form-message" role="status">
                {message}
              </p>
            )}
            <button
              className="submit-button"
              type="submit"
              disabled={submitting}
            >
              {submitting ? "Logging in…" : "Log in"}
            </button>
          </form>
          <p className="form-switch">
            New here?{" "}
            <button type="button" onClick={onSwitchToSignUp}>
              Sign up
            </button>
          </p>
        </div>
      </main>
    </div>
  );
}