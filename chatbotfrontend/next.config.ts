import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  // Enable WebSocket proxying for development
  async rewrites() {
    return [
      {
        source: "/chat-websocket", // The WebSocket endpoint the frontend connects to
        destination: "http://localhost:8080/chat-websocket", // Backend WebSocket server
      },
    ];
  },

  // CORS headers for API routes if necessary
  async headers() {
    return [
      {
        source: "/api/:path*", // Apply CORS headers to API routes
        headers: [
          {
            key: "Access-Control-Allow-Origin",
            value: "http://localhost:8080", // Backend origin
          },
          {
            key: "Access-Control-Allow-Methods",
            value: "GET, POST, OPTIONS",
          },
          {
            key: "Access-Control-Allow-Headers",
            value: "Content-Type, Authorization",
          },
        ],
      },
    ];
  },
};

export default nextConfig;
