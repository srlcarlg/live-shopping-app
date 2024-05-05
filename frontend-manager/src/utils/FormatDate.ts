export const formatTimestamp = (timestamp: string): string => {
  const date = new Date(timestamp);
  const formattedDate = date.toLocaleString();
  return formattedDate;
};