import React from "react";
import { CREATE_LIVE_POST } from "../../api/api";
import InputModal from "./Input";

type Props = {
  isEdit?: boolean;
  cancelOnClick: (value: boolean) => void;
};

const LiveModal = (props: Props) => {
  const { isEdit: isEdit } = props;

  // Modal - Cancel button
  const [cancelClick, setCancelClick] = React.useState(false);
  const handleCancel = () => {
    props.cancelOnClick(cancelClick);
    setCancelClick(!cancelClick);
  };

  // Modal - Add live
  const [formData, setFormData] = React.useState({
    title: "The Greatest Live",
    description:
      "Lorem ipsum dolor sit amet consectetur adipisicing elit. Eos pariatur cum recusandae nostrum optio odio voluptates repellat! Corrupti numquam iste modi quibusdam in ipsum cupiditate, maiores ut doloremque nulla impedit.",
    password: "123456",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      const { url, options } = CREATE_LIVE_POST(formData);
      const response = await fetch(url, options);
      const data: Live = await response.json();

      if (response.ok) {
        props.cancelOnClick(cancelClick);
        setCancelClick(!cancelClick);
        console.log(data);
      }
    } catch (error) {
      console.log(error);
    }
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
              <div className="row">
                <form id="live-form" onSubmit={handleSubmit}>
                  <InputModal
                    className="live-title-input"
                    name="title"
                    placeholder="Title"
                    value={formData.title}
                    onChange={handleChange}
                  />
                  <InputModal
                    className="live-password-input"
                    name="password"
                    placeholder="Password"
                    value={formData.password}
                    onChange={handleChange}
                  />
                </form>
              </div>
              <div className="second-row">
                <form id="live-form" onSubmit={handleSubmit}>
                  <InputModal
                    className="live-description-input"
                    name="description"
                    placeholder="Description"
                    value={formData.description}
                    onChange={handleChange}
                  />
                </form>
              </div>
            </div>
          ) : (
            <div className="row-inputs">
              <div className="first-column">
                <form id="live-form" onSubmit={handleSubmit}>
                  <InputModal
                    className="live-password-input"
                    name="password"
                    placeholder="Password"
                    value={formData.password}
                    onChange={handleChange}
                  />
                </form>
              </div>
            </div>
          )}
          <div className="btn-area">
            <button className="btn btn-cancel" onClick={handleCancel}>
              Cancel
            </button>
            <button
              type="submit"
              form="live-form"
              className="btn btn-live-submit"
            >
              Confirm
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LiveModal;
