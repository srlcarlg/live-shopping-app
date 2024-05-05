import React, { ChangeEvent } from "react";

interface Props {
  className?: string;
  placeholder?: string;
  value: string;
  name: string; // Add the name attribute
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const Input = (props: Props) => {
  const { className, placeholder, value, name, onChange } = props;

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    onChange(e);
  };

  return (
    <input
      id="any-input-modal"
      className={className}
      type="text"
      placeholder={placeholder}
      value={value}
      name={name} // Pass the name attribute to the input element
      onChange={handleChange}
    />
  );
};

export default Input;
