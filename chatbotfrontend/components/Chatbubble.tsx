"use client";


import { useState, useEffect, useRef } from "react";
import { motion, AnimatePresence} from "framer-motion"
import { Client, IMessage } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { timeStamp } from "console";

interface ChatMessage {
    text: string;
    timestamp: string;
    sender: "user" | "bot";
}

export default function ChatApp() {
    const [isChatVisible, setChatVisible] = useState<boolean>(false);
    const [messages, setMessages] = useState<ChatMessage[]>([
        {
            text: "Hello! How can I assist you today?",
            timestamp: "",
            sender: "bot"
        }
    ]);
    const [messageText, setMessageText] = useState<string>("");

    const stompClientRef = useRef<Client | null>(null);
    const messagesEndRef = useRef<HTMLDivElement | null>(null);

    // Function to toggle the chat visibility
    const toggleChat = (): void => {
        setChatVisible(!isChatVisible);
    };

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({behavior: "smooth"});
    };

    useEffect(() => {
        scrollToBottom();
      }, [messages]);

    // Initialize STOMP client
    useEffect(() => {
        const connectStomp = () => {
            const client = new Client({
                brokerURL: "ws://localhost:8080/chat-websocket", // Use SockJS URL in case of fallback
                connectHeaders: {
                    // Optional headers, such as authentication tokens
                },
                debug: (str) => {
                    console.log(str);
                },
                reconnectDelay: 5000, // Auto-reconnect in case of disconnection
                webSocketFactory: () => new SockJS("http://localhost:8080/chat-websocket"), // SockJS fallback
                onConnect: () => {
                    console.log("STOMP connection established.");

                    // Subscribe to a topic
                    if (stompClientRef.current) {
                        stompClientRef.current.subscribe("/topic/messages", (message) => {
                            const newMessage: ChatMessage = JSON.parse(message.body);
                            setMessages((prev) => [...prev, newMessage]);
                        });
                    }
                },
                onStompError: (frame) => {
                    console.error("STOMP error: ", frame.headers["message"]);
                },
            });

            stompClientRef.current = client;
            client.activate();
        };

        connectStomp();

        return () => {
            if (stompClientRef.current) {
                stompClientRef.current.deactivate();
            }
        };
    }, []);

    // Function to send a message
    const handleSendMessage = (): void => {
        if (!messageText.trim()) {
            console.warn("Cannot send an empty message.");
            return;
        }

        const newMessage: ChatMessage = {
            text: messageText,
            timestamp: new Date().toISOString(),
            sender: "user",
        };

        // Send message to the backend
        if (stompClientRef.current && stompClientRef.current.connected) {
            stompClientRef.current.publish({
                destination: "/app/ai/generate", // Backend endpoint to handle messages
                body: JSON.stringify({
                    text: messageText,
                    timestamp: new Date().toLocaleTimeString([], {
                        hour: "2-digit",
                        minute: "2-digit",
                      }),
                    sender: "user"
                }
                ),
            });
        } else {
            console.error("STOMP client is not connected.");
        }

        // Optimistic rendering
        setMessages((prev) => [...prev, newMessage]);
        setMessageText(""); // Clear input field
    };

    return (
        <div>
          {/* Chat Button */}
          <button
            onClick={toggleChat}
            className="fixed bottom-4 right-4 bg-blue-500 text-white p-3 rounded-full shadow-lg hover:bg-blue-600"
          >
            Chat
          </button>
    
          {/* Chat Window with AnimatePresence */}
          <AnimatePresence>
            {isChatVisible && (
              <motion.div
                key="chat"
                initial={{ opacity: 0, y: 50, scale: 0.95 }}
                animate={{ opacity: 1, y: 0, scale: 1 }}
                exit={{ opacity: 0, y: 50, scale: 0.95 }}
                transition={{ duration: 0.2 }}
                className="fixed bottom-20 right-4 w-80 h-96 bg-white border-2 border-gray-300 rounded-lg shadow-lg flex flex-col"
              >
                {/* Header */}
                <div className="flex justify-between items-center border-b p-2">
                  <h2 className="text-lg font-bold">Chat</h2>
                  <button
                    onClick={toggleChat}
                    className="text-gray-500 hover:text-gray-700"
                  >
                    ✖
                  </button>
                </div>
    
                {/* Messages Area */}
                <div className="flex-grow p-2 overflow-y-auto space-y-3">
                  {messages.map((message, index) => (
                    <div
                      key={index}
                      className={`flex ${
                        message.sender === "user" ? "justify-end" : "justify-start"
                      }`}
                    >
                      <div
                        className={`max-w-xs p-3 rounded-lg text-sm ${
                          message.sender === "user"
                            ? "bg-blue-500 text-white rounded-br-lg" // User message styles
                            : "bg-gray-200 text-gray-800 rounded-bl-lg" // Bot message styles
                        }`}
                      >
                        <p className="mb-1">{message.text}</p>
                        <span className="text-xs text-gray-500">
                          {message.timestamp
                            ? new Date(message.timestamp).toLocaleTimeString()
                            : ""}
                        </span>
                      </div>
                    </div>
                  ))}
                  <div ref={messagesEndRef} /> {/* Dummy div for scrolling */}
                </div>
    
                {/* Input and Send Button */}
                <div className="flex items-center border-t p-2 space-x-2">
                  <input
                    type="text"
                    value={messageText}
                    onChange={(e) => setMessageText(e.target.value)}
                    placeholder="Type a message..."
                    className="flex-grow p-2 border border-gray-300 rounded-xl"
                  />
                  <button
                    onClick={handleSendMessage}
                    className="bg-blue-500 text-white p-2 rounded-lg hover:bg-blue-600"
                  >
                    Send
                  </button>
                </div>
              </motion.div>
            )}
          </AnimatePresence>
        </div>
      );

}
