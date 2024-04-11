import AmountInfo from "./AmountInfo";

type Props = {};

const Amount = (props: Props) => {
  return (
    <div className="all-transactions">
      <div className="transactions-header">
        <h3>Total Amount per Live</h3>
      </div>
      <div className="table-grid">
        <table>
          <tr className="table-header">
            <td>Live Title</td>
            <td>nº Transactions</td>
            <td>Total</td>
          </tr>
          <AmountInfo />
          <AmountInfo />
          <AmountInfo />
          <AmountInfo />
          <AmountInfo />
          <AmountInfo />
          <AmountInfo />
        </table>
      </div>
    </div>
  );
};

export default Amount;
