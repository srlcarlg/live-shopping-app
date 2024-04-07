import chatIcon from "../assets/chat-icon.svg";
import sendIcon from "../assets/send-icon.svg";
import shopIcon from "../assets/shop-icon.svg";

type Props = {};

const Chat = (props: Props) => {
  return (
    <div className="chat">
      <div className="shape">
        <div className="chat-header">
          <nav>
            <ul>
              <li>
                <button className="btn btn-chat">
                  <div className="text">
                    <img src={chatIcon} alt="" />
                    Chat
                  </div>
                </button>
              </li>
              <li>
                <button className="btn btn-shop">
                  <div className="text">
                    <img src={shopIcon} alt="" />
                    Shop
                  </div>
                </button>
              </li>
            </ul>
          </nav>
        </div>
        <div className="border-and-input">
          <div className="border"></div>
          <div className="input-container">
            <div className="input-area">
              <img src={chatIcon} alt="" className="avatar" />
              <div className="input-column">
                <p>Broadcaster</p>
                <input type="text" placeholder="Say something..." />
              </div>
              <img src={sendIcon} alt="" className="input-send-icon" />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Chat;
