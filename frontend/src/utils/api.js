export const backendHost =
  window.location.hostname === "localhost"
    ? "http://localhost:8070"
    : `http://${window.location.hostname}:8070`;