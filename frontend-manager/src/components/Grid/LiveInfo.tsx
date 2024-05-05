import React from "react";
import broadcastIcon from "../../assets/broadcast-icon.svg";
import editIcon from "../../assets/edit-icon.svg";
import viewIcon from "../../assets/view-icon.svg";
import { formatTimestamp } from "../../utils/FormatDate";
import LiveModal from "../Modals/LiveModal";

type Props = {
  live: Live;
};

const LiveInfo = (props: Props) => {
  const [modalIsOpen, setModalIsOpen] = React.useState(false);
  const handleModalCancelClick = (clicked: boolean) => {
    setModalIsOpen(clicked);
  };
  const { live } = props;
  return (
    <>
      <td>{live.title}</td>
      <td>{live.slug}</td>
      <td>{live.status}</td>
      <td>{formatTimestamp(live.createdAt)}</td>
      <td>
        <div className="actions">
          <a
            href={`${import.meta.env.VITE_STREAMING_URL}/broadcast/${
              live.slug
            }`}
            target="_blank"
          >
            <img src={broadcastIcon} alt="" />
            Broadcast
          </a>
          <a
            href={`${import.meta.env.VITE_STREAMING_URL}/view/${live.slug}`}
            target="_blank"
          >
            <img src={viewIcon} alt="" />
            View
          </a>
          <button className="btn btn-add" onClick={() => setModalIsOpen(true)}>
            <div className="text">
              <img src={editIcon} alt="" />
              Edit
            </div>
          </button>
          {modalIsOpen ? (
            <LiveModal isEdit={true} cancelOnClick={handleModalCancelClick} />
          ) : (
            <></>
          )}
        </div>
      </td>
    </>
  );
};

export default LiveInfo;
