import { useState } from "react";

export default function SignUpPage({ onSwitchToLogin }) {
  const [form, setForm] = useState({
    displayName: "",
    email: "",
    password: "",
  });
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");
    setSubmitting(true);

    try {
      const res = await fetch("/api/auth/register", {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });

      const responseText = await res.text();
      let data = {};
      try {
        data = responseText ? JSON.parse(responseText) : {};
      } catch {
        data = { message: responseText };
      }

      if (!res.ok) {
        setError(
          data.detail ||
            data.message ||
            data.error ||
            "Could not create account. Please try again.",
        );
        return;
      }

      localStorage.setItem("tambayanGuildUser", JSON.stringify(data));
      setSuccess("Account created! You can now log in.");
      setForm({ displayName: "", email: "", password: "" });
    } catch {
      setError("Unable to reach server on port 8081.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="min-h-screen grid grid-cols-1 lg:grid-cols-[42%_58%] bg-[#f4f3fa] text-[#111827]">
      {/* Left Brand Panel */}
      <aside className="flex flex-col justify-center px-8 py-12 sm:px-14 lg:px-16 bg-[#5943e3] text-white">
        <div className="max-w-md w-full mx-auto lg:mx-0">
          <div className="w-11 h-11 rounded-full bg-white/15 flex items-center justify-center mb-7">
            <span className="text-lg" aria-hidden="true">
              🛡️
            </span>
          </div>

          <h1 className="text-4xl sm:text-[42px] font-extrabold tracking-tight leading-[1.14] mb-4">
            Study together.
            <br />
            Level up together.
          </h1>

          <p className="text-sm sm:text-[15px] text-indigo-100/90 leading-relaxed mb-7">
            Tambayan Guild turns group study into a team campaign with shared
            decks, live trivia, and daily quests.
          </p>

          <ul className="space-y-3 text-sm text-indigo-50">
            <li className="flex items-center gap-2.5">
              <span aria-hidden="true">👥</span>
              <span>Shared flashcard decks</span>
            </li>
            <li className="flex items-center gap-2.5">
              <span aria-hidden="true">⚡</span>
              <span>Real-time multiplayer trivia</span>
            </li>
            <li className="flex items-center gap-2.5">
              <span aria-hidden="true">🔥</span>
              <span>Daily quests, XP &amp; leaderboards</span>
            </li>
          </ul>
        </div>
      </aside>

      {/* Right Sign-Up Form Panel */}
      <main className="flex items-center justify-center px-6 py-12 sm:px-12">
        <div className="w-full max-w-[380px]">
          <div className="mb-7">
            <h2 className="text-[28px] font-bold tracking-tight text-[#111827] mb-1.5">
              Create your account
            </h2>
            <p className="text-xs sm:text-sm text-slate-500">
              Join a guild and start earning XP with your classmates.
            </p>
          </div>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label
                htmlFor="displayName"
                className="block text-xs font-medium text-slate-700 mb-1.5"
              >
                Display name
              </label>
              <input
                id="displayName"
                name="displayName"
                type="text"
                required
                placeholder="e.g. Alex the Scholar"
                value={form.displayName}
                onChange={handleChange}
                className="w-full rounded-lg border border-slate-200 bg-white px-3.5 py-2.5 text-sm text-slate-900 placeholder:text-slate-400 focus:border-[#5943e3] focus:outline-none focus:ring-2 focus:ring-[#5943e3]/20"
              />
            </div>

            <div>
              <label
                htmlFor="email"
                className="block text-xs font-medium text-slate-700 mb-1.5"
              >
                School email
              </label>
              <input
                id="email"
                name="email"
                type="email"
                required
                placeholder="you@school.edu"
                value={form.email}
                onChange={handleChange}
                className="w-full rounded-lg border border-slate-200 bg-white px-3.5 py-2.5 text-sm text-slate-900 placeholder:text-slate-400 focus:border-[#5943e3] focus:outline-none focus:ring-2 focus:ring-[#5943e3]/20"
              />
            </div>

            <div>
              <label
                htmlFor="password"
                className="block text-xs font-medium text-slate-700 mb-1.5"
              >
                Password
              </label>
              <input
                id="password"
                name="password"
                type="password"
                required
                minLength={8}
                placeholder="At least 8 characters"
                value={form.password}
                onChange={handleChange}
                className="w-full rounded-lg border border-slate-200 bg-white px-3.5 py-2.5 text-sm text-slate-900 placeholder:text-slate-400 focus:border-[#5943e3] focus:outline-none focus:ring-2 focus:ring-[#5943e3]/20"
              />
            </div>

            {error && (
              <p className="text-xs font-medium text-red-600">{error}</p>
            )}
            {success && (
              <p className="text-xs font-medium text-emerald-700">{success}</p>
            )}

            <button
              type="submit"
              disabled={submitting}
              className="w-full rounded-lg bg-[#5943e3] hover:bg-[#4934cc] disabled:opacity-60 text-white font-semibold text-sm py-2.5 px-4 transition-colors cursor-pointer mt-1"
            >
              {submitting ? "Creating account..." : "Create account"}
            </button>
          </form>

          <p className="mt-5 text-center text-xs text-slate-500">
            Already have an account?{" "}
            <button
              type="button"
              onClick={onSwitchToLogin}
              className="font-semibold text-[#5943e3] hover:underline cursor-pointer"
            >
              Log in
            </button>
          </p>
        </div>
      </main>
    </div>
  );
}