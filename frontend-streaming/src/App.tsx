import React from "react";
import { CHAT_WS_URL } from "./api/chat/api";
import { FIND_LIVE_GET, MANAGER_WS_URL } from "./api/manager/api";
import Chat from "./components/Chat";
import Description from "./components/Description";
import Header from "./components/Header";
import ErrorModal from "./components/Modals/ErrorModal";
import LoginModal from "./components/Modals/LoginModal";
import Video from "./components/Video";
import { SessionProvider } from "./context/SessionProvider";
import "./styles/index.scss";

export interface TabInfo {
  isBroadcaster: boolean;
  live: Live;
}

function App() {
  const [tabInfo, setTabInfo] = React.useState<TabInfo>();

  const [error, setError] = React.useState(false);
  const [errorMsg, setErrorMsg] = React.useState("");

  const handleError = (msg: string) => {
    setError(true);
    setErrorMsg(msg);
  };
  // Some validations
  React.useEffect(() => {
    const parts = window.location.pathname.split("/");
    const route = parts[1];
    const slug = parts[2];

    if (route == null || (route !== "view" && route !== "broadcast")) {
      handleError(`Route /${route} doesn't exist!`);
    } else if (slug == null || slug === "") {
      handleError("Slug /{null} doesn't exist!");
    }

    const fetchLive = async () => {
      try {
        const { url, options } = FIND_LIVE_GET(slug);
        const response = await fetch(url, options);
        if (response.status === 200) {
          const data: Live = await response.json();
          setTabInfo({
            isBroadcaster: route === "broadcast",
            live: data,
          });
        } else {
          handleError(`Slug /${slug} not found!`);
        }
      } catch (err) {
        handleError(`${err} while fetching to Manager API`);
      }
    };
    fetchLive();
  }, []);

  // Websocket client
  const [wsManager, setWsManager] = React.useState<WebSocket>();
  const [wsChat, setWsChat] = React.useState<WebSocket>();

  React.useEffect(() => {
    const socketManager = new WebSocket(
      MANAGER_WS_URL + "/" + tabInfo?.live.slug
    );
    const socketChat = new WebSocket(CHAT_WS_URL + "/" + tabInfo?.live.slug);

    // Connection opened
    socketManager.addEventListener("open", () => {
      console.log("WebSocket MANAGER connected");
      setWsManager(socketManager);
    });
    socketChat.addEventListener("open", () => {
      console.log("WebSocket CHAT connected");
      setWsChat(socketChat);
    });

    // Clean up on unmount
    return () => {
      if (
        socketManager.readyState === WebSocket.OPEN ||
        socketManager.readyState === WebSocket.CONNECTING
      ) {
        socketManager.close();
      }
      if (
        socketChat.readyState === WebSocket.OPEN ||
        socketChat.readyState === WebSocket.CONNECTING
      ) {
        socketChat.close();
      }
    };
  }, [tabInfo]);

  const [modalIsClose, setModalIsClose] = React.useState(false);
  const handleModalSubmitClick = (clicked: boolean) => {
    setModalIsClose(clicked);
  };

  return (
    <>
      <SessionProvider>
        {error ? (
          <ErrorModal msg={errorMsg} />
        ) : !modalIsClose ? (
          <LoginModal
            isBroadcaster={tabInfo?.isBroadcaster}
            wsManager={wsManager}
            wsChat={wsChat}
            submitOnClick={handleModalSubmitClick}
          />
        ) : (
          <></>
        )}
        <Header isBroadcaster={tabInfo?.isBroadcaster} />
        <section className="video-and-chat">
          <div className="container">
            <Video
              isBroadcaster={tabInfo?.isBroadcaster}
              wsManager={wsManager}
            />
            <Chat
              isBroadcaster={tabInfo?.isBroadcaster}
              live={tabInfo?.live}
              wsChat={wsChat}
            />
          </div>
        </section>
        <Description wsManager={wsManager} live={tabInfo?.live} />
      </SessionProvider>
    </>
  );
}

export default App;
