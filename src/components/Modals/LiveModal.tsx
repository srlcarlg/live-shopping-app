import React from "react";
import InputModal from "./Input";

type Props = {
  isEdit?: boolean;
  cancelOnClick: (value: boolean) => void;
};

const LiveModal = (props: Props) => {
  const { isEdit: isEdit } = props;
  const [cancelClick, setCancelClick] = React.useState(false);
  const handleCancel = () => {
    props.cancelOnClick(cancelClick);
    setCancelClick(!cancelClick);
  };

  return (
    <div className="modal-screen" id="any-modal">
      <div className="modal-content">
        <div className="title">
          <h3>{!isEdit ? "Create New Live" : "Edit"}</h3>
          {!isEdit ? <div></div> : <h4>The Great Live</h4>}
        </div>
        <div className="modal-inputs">
          {!isEdit ? (
            <div className="row-inputs">
              <div className="first-column">
                <InputModal
                  className="live-title-input"
                  placeholder="Title"
                  onChange={() => {}}
                />
                <InputModal
                  className="live-description-input"
                  placeholder="Description"
                  onChange={() => {}}
                />
              </div>
              <div className="colunm">
                <InputModal
                  className="live-slug-input"
                  placeholder="Slug"
                  onChange={() => {}}
                />
                <InputModal
                  className="live-password-input"
                  placeholder="Password"
                  onChange={() => {}}
                />
              </div>
            </div>
          ) : (
            <div className="row-inputs">
              <div className="first-column">
                <InputModal
                  className="live-password-input"
                  placeholder="Password"
                  onChange={() => {}}
                />
              </div>
            </div>
          )}
          <div className="btn-area">
            <button className="btn btn-cancel" onClick={handleCancel}>
              Cancel
            </button>
            <button type="submit" className="btn btn-live-submit">
              Confirm
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LiveModal;
