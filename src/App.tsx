import { useState } from "react";
import Description from "./components/Description";
import Header from "./components/Header";
import Video from "./components/Video";
import "./styles/index.scss";

function App() {
  const [count, setCount] = useState(0);

  return (
    <>
      <Header />
      <Video />
      <Description />
    </>
  );
}

export default App;
