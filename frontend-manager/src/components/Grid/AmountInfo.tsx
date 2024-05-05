type Props = {
  amount: Amount;
};

const AmountInfo = (props: Props) => {
  const { amount } = props;
  return (
    <>
      <td>{amount.liveSlug}</td>
      <td>{amount.nTransactions}</td>
      <td>${amount.total}</td>
    </>
  );
};

export default AmountInfo;
