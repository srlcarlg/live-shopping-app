import { useState } from "react";
import Chat from "./components/Chat";
import Description from "./components/Description";
import Header from "./components/Header";
import Video from "./components/Video";
import "./styles/index.scss";

function App() {
  const [count, setCount] = useState(0);

  return (
    <>
      <Header />
      <section className="video-and-chat">
        <div className="container">
          <Video />
          <Chat />
        </div>
      </section>
      <Description />
    </>
  );
}

export default App;
