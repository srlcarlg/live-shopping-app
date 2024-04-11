import TransactionsInfo from "./TransactionsInfo";

type Props = {};

const Transactions = (props: Props) => {
  return (
    <div className="all-transactions">
      <div className="transactions-header">
        <h3>Latest Transactions</h3>
      </div>
      <div className="table-grid">
        <table>
          <tr className="table-header">
            <td>Id</td>
            <td>Live Slug</td>
            <td>Date</td>
            <td>Amount</td>
          </tr>
          <TransactionsInfo />
          <TransactionsInfo />
          <TransactionsInfo />
          <TransactionsInfo />
          <TransactionsInfo />
          <TransactionsInfo />
          <TransactionsInfo />
          <TransactionsInfo />
        </table>
      </div>
    </div>
  );
};

export default Transactions;
