import React from "react";
import InputModal from "./Input";

type Props = {};

const LoginModal = (props: Props) => {
  const [isBroadcaster, setIsBroadcaster] = React.useState(false);

  return (
    <div className="modal-screen" id="any-modal">
      <div className="modal-content">
        <div className="title">
          <h3>Great Something </h3>
          <h4>Login to {isBroadcaster ? "broadcast" : "view"}</h4>
        </div>

        <div className="modal-inputs">
          <InputModal
            className="login-input-modal"
            placeholder="Name"
            onChange={() => {}}
          />
          <InputModal
            className="login-input-modal"
            placeholder="Email"
            onChange={() => {}}
          />
          {isBroadcaster ? (
            <InputModal
              className="login-input-modal"
              placeholder="Live Password"
              onChange={() => {}}
            />
          ) : (
            <div></div>
          )}
          <div className="btn-area">
            <button type="submit" className="btn btn-login-submit">
              Submit
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginModal;
