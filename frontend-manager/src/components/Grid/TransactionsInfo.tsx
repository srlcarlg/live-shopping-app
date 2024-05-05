import { formatTimestamp } from "../../utils/FormatDate";

type Props = {
  transaction: Transaction;
};

const TransactionsInfo = (props: Props) => {
  const { transaction } = props;
  return (
    <>
      <td>{transaction.uuid}</td>
      <td>{transaction.liveSlug}</td>
      <td>{formatTimestamp(transaction.createdAt)}</td>
      <td>${transaction.amount}</td>
    </>
  );
};

export default TransactionsInfo;
