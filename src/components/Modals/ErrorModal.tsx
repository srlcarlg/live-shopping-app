type Props = {
  msg: string;
};

const ErrorModal = (props: Props) => {
  return (
    <div className="modal-screen" id="any-modal">
      <div className="modal-content">
        <div className="title">
          <h3>Error</h3>
          <h4>Something wrong isn't right</h4>
        </div>
        <div className="modal-inputs">
          <h5>{props.msg}</h5>
        </div>
      </div>
    </div>
  );
};

export default ErrorModal;
