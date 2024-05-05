import React from "react";
import { MessageChat, MessageIncoming } from "../api/chat/models";
import chatIcon from "../assets/chat-icon.svg";
import sendIcon from "../assets/send-icon.svg";
import shopIcon from "../assets/shop-icon.svg";
import Shop from "./Chat/Shop";

type Props = {
  isBroadcaster?: boolean;
  live?: Live;
  wsChat?: WebSocket;
};

const Chat = (props: Props) => {
  const [currentTab, setCurrentTab] = React.useState("chat");
  const handleTabs = async (value: string) => {
    setCurrentTab(value);
  };

  const { isBroadcaster, live, wsChat } = props;

  const [input, setInput] = React.useState("");
  const [messages, setMessages] = React.useState<MessageIncoming[]>([]);

  React.useEffect(() => {
    wsChat?.addEventListener("message", (event) => {
      try {
        const incomingMessage: MessageIncoming = JSON.parse(event.data);
        if (incomingMessage.message) {
          // Filter out messages that are the same as the sent message
          setMessages((prevMessages) => [...prevMessages, incomingMessage]);
        }
      } catch (error) {}
    });
  }, [live, wsChat]);

  const sendMessage = () => {
    const messageOutgoing: MessageChat = {
      message: input,
    };

    wsChat?.send(JSON.stringify(messageOutgoing));
    setInput("");
  };

  return (
    <div className="chat">
      <div className="shape">
        <div className="chat-header">
          <nav>
            <ul>
              <li>
                <button
                  className="btn btn-chat"
                  onClick={() => handleTabs("chat")}
                >
                  <div className="text">
                    <img src={chatIcon} alt="" />
                    Chat
                  </div>
                </button>
              </li>
              <li>
                <button
                  className="btn btn-shop"
                  onClick={() => handleTabs("shop")}
                >
                  <div className="text">
                    <img src={shopIcon} alt="" />
                    Shop
                  </div>
                </button>
              </li>
            </ul>
          </nav>
        </div>
        <div className="border-container">
          {currentTab === "chat" ? (
            <div className="border">
              {messages.map((msg, index) =>
                msg.username ? (
                  <div key={index} className="message">
                    <div
                      className={`profile ${
                        msg.isBroadcaster ? "broadcaster" : ""
                      }`}
                    >
                      {msg.username.charAt(0).toUpperCase()}
                    </div>
                    <div className="message-content">
                      <span
                        className={`username ${
                          msg.isBroadcaster ? "highlight" : ""
                        }`}
                      >
                        {msg.username}
                      </span>
                      {msg.message}
                    </div>
                  </div>
                ) : (
                  <div> </div>
                )
              )}
            </div>
          ) : (
            <div className="border-shop">
              <Shop isBroadcaster={props.isBroadcaster} live={props.live} />
            </div>
          )}
        </div>
        {currentTab === "chat" ? (
          <div className="input-container">
            <div className="input-area">
              <img src={chatIcon} alt="" className="avatar" />
              <div className="input-column">
                <span>{isBroadcaster ? "Broadcaster" : "Viewer"}</span>
                <input
                  type="text"
                  value={input}
                  placeholder="Say something..."
                  onChange={(e) => setInput(e.target.value)}
                  onKeyDown={(event) => {
                    if (event.key === "Enter") {
                      sendMessage();
                    }
                  }}
                />
              </div>
              <button onClick={sendMessage}>
                <img src={sendIcon} alt="" className="input-send-icon" />
              </button>
            </div>
          </div>
        ) : (
          <div></div>
        )}
      </div>
    </div>
  );
};

export default Chat;
