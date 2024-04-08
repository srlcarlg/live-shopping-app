import React from "react";
import chatIcon from "../assets/chat-icon.svg";
import sendIcon from "../assets/send-icon.svg";
import shopIcon from "../assets/shop-icon.svg";
import Shop from "./Chat/Shop";

type Props = {};

const Chat = (props: Props) => {
  const [currentTab, setCurrentTab] = React.useState("chat");

  const handleTabs = async (value: string) => {
    setCurrentTab(value);
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
            <div className="border"></div>
          ) : (
            <div className="border-shop">
              <Shop />
            </div>
          )}
        </div>
        {currentTab === "chat" ? (
          <div className="input-container">
            <div className="input-area">
              <img src={chatIcon} alt="" className="avatar" />
              <div className="input-column">
                <span>Broadcaster</span>
                <input type="text" placeholder="Say something..." />
              </div>
              <img src={sendIcon} alt="" className="input-send-icon" />
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
