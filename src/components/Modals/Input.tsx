import { ChangeEvent } from "react";

interface Props {
  className?: string;
  placeholder?: string;
  onChange: (value: string) => void;
}

const Input = (props: Props) => {
  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    props.onChange(e.target.value);
  };

  return (
    <input
      id="any-input-modal"
      className={props.className}
      type="text"
      placeholder={props.placeholder}
      onChange={handleChange}
    />
  );
};

export default Input;
