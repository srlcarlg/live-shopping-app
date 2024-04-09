import { useState } from "react";
import PaymentModal from "./components/Modals/PaymentModal";
import "./styles/index.scss";

function App() {
  const [count, setCount] = useState(0);

  return (
    <>
      {/* <Header />
      <section className="video-and-chat">
        <div className="container">
          <Video />
          <Chat />
        </div>
      </section>
      <Description /> */}
      <PaymentModal />
    </>
  );
}

export default App;
