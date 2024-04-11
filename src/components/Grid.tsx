import Amount from "./Grid/Amount";
import Lives from "./Grid/Lives";
import Transactions from "./Grid/Transactions";

type Props = {};

const Grid = (props: Props) => {
  return (
    <div className="container">
      <div className="grid">
        <Lives />
        <div className="grid-row">
          <Transactions />
          <Amount />
        </div>
      </div>
    </div>
  );
};

export default Grid;
