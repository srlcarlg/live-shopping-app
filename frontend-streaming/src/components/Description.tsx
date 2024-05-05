import React from "react";

type Props = {
  wsManager?: WebSocket;
  live?: Live;
};

interface Count {
  users_count: number;
}

const Description = (props: Props) => {
  const [count, setCount] = React.useState(0);

  const { wsManager, live } = props;
  React.useEffect(() => {
    // Listen for messages
    wsManager?.addEventListener("message", (event) => {
      try {
        const users: Count = JSON.parse(event.data);
        if (users.users_count) {
          setCount(users.users_count);
        }
      } catch (error) {}
    });
  }, [wsManager]);

  return (
    <section className="description">
      <div className="container">
        <div className="text">
          <h1>{live?.title}</h1>
          <h2>{count} Watching</h2>
          <p>{live?.description}</p>
        </div>
      </div>
    </section>
  );
};

export default Description;
