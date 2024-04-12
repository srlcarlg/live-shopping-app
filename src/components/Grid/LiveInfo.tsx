import React from "react";
import broadcastIcon from "../../assets/broadcast-icon.svg";
import editIcon from "../../assets/edit-icon.svg";
import viewIcon from "../../assets/view-icon.svg";
import LiveModal from "../Modals/LiveModal";

type Props = {};

const LiveInfo = (props: Props) => {
  const [modalIsOpen, setModalIsOpen] = React.useState(false);

  const handleModalCancelClick = (clicked: boolean) => {
    setModalIsOpen(clicked);
  };

  return (
    <>
      {modalIsOpen ? (
        <LiveModal isEdit={true} cancelOnClick={handleModalCancelClick} />
      ) : (
        <></>
      )}
      <tr>
        <td>ac77bf30-b18d-439d-ba44-87d7cbcb6aba</td>
        <td>The Great Live</td>
        <td>/87d7cbcb6aba</td>
        <td>AVAILABLE</td>
        <td>10 Apr 2024</td>
        <td>
          <div className="actions">
            <a href="">
              <img src={broadcastIcon} alt="" />
              Broadcast
            </a>
            <a href="">
              <img src={viewIcon} alt="" />
              View
            </a>
            <button
              className="btn btn-add"
              onClick={() => setModalIsOpen(true)}
            >
              <div className="text">
                <img src={editIcon} alt="" />
                Edit
              </div>
            </button>
          </div>
        </td>
      </tr>
    </>
  );
};

export default LiveInfo;
