import React from "react";
import addIcon from "../../assets/add-icon.svg";
import LiveModal from "../Modals/LiveModal";
import LiveInfo from "./LiveInfo";

type Props = {};

const Lives = (props: Props) => {
  const [modalIsOpen, setModalIsOpen] = React.useState(false);

  const handleModalCancelClick = (clicked: boolean) => {
    setModalIsOpen(clicked);
  };

  return (
    <>
      {modalIsOpen ? (
        <LiveModal isEdit={false} cancelOnClick={handleModalCancelClick} />
      ) : (
        <></>
      )}
      <div className="all-lives">
        <div className="live-header">
          <h2>All Lives</h2>
          <button className="btn btn-add" onClick={() => setModalIsOpen(true)}>
            <div className="text">
              <img src={addIcon} alt="" />
              Add
            </div>
          </button>
        </div>
        <div className="table-grid">
          <table>
            <tr className="table-header">
              <td>Id</td>
              <td>Title</td>
              <td>Slug</td>
              <td>Status</td>
              <td>Date</td>
              <td>Actions</td>
            </tr>
            <LiveInfo />
            <LiveInfo />
            <LiveInfo />
            <LiveInfo />
            <LiveInfo />
            <LiveInfo />
            <LiveInfo />
            <LiveInfo />
            <LiveInfo />
          </table>
        </div>
      </div>
    </>
  );
};

export default Lives;
