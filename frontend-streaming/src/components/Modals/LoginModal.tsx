import React from "react";
import {
  JoinChatBroadcaster,
  JoinChatViewer,
  SessionBroadcast,
} from "../../api/chat/models";
import { useSession } from "../../context/SessionContext";
import InputModal from "./Input";

type Props = {
  isBroadcaster?: boolean;
  wsManager?: WebSocket;
  wsChat?: WebSocket;
  submitOnClick: (value: boolean) => void;
};

const LoginModal = (props: Props) => {
  const [formData, setFormData] = React.useState({
    username: "",
    email: "",
    password: "",
  });
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const { wsManager, wsChat } = props;
  const { setSessionId, setLivePassword } = useSession();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    wsManager?.send("join");

    if (formData.password === "") {
      if (formData.username !== "" && formData.email !== "") {
        const send: JoinChatViewer = formData;
        wsChat?.send(JSON.stringify(send));
        props.submitOnClick(true);
      }
    } else {
      if (formData.username !== "" && formData.email !== "") {
        const sessionIdEvent = wsChat?.addEventListener("message", (event) => {
          try {
            if (event.data !== "incorrect-password") {
              const ssId: SessionBroadcast = JSON.parse(event.data);

              if (ssId.sessionId) {
                console.log(ssId);
                setSessionId(ssId.sessionId);
                setLivePassword(formData.password);

                wsChat?.removeEventListener("message", sessionIdEvent!);
                props.submitOnClick(true);
              }
            } else {
            }
          } catch (error) {}
        });

        const send: JoinChatBroadcaster = formData;
        console.log(send);
        wsChat?.send(JSON.stringify(send));
      }
    }
  };

  return (
    <div className="modal-screen" id="any-modal">
      <div className="modal-content">
        <div className="title">
          <h3>Great Something </h3>
          <h4>Login to {props.isBroadcaster ? "broadcast" : "view"}</h4>
        </div>
        <div className="modal-inputs">
          <form id="login-form" onSubmit={handleSubmit}>
            <InputModal
              className="login-input-modal"
              placeholder="Username"
              name="username"
              value={formData.username}
              onChange={handleChange}
            />
            <InputModal
              className="login-input-modal"
              placeholder="Email"
              name="email"
              value={formData.email}
              onChange={handleChange}
            />
            {props.isBroadcaster ? (
              <InputModal
                className="login-input-modal"
                placeholder="Live Password"
                name="password"
                value={formData.password}
                onChange={handleChange}
              />
            ) : (
              <div></div>
            )}
            <div className="btn-area">
              <button
                type="submit"
                form="login-form"
                className="btn btn-login-submit"
              >
                Submit
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default LoginModal;
