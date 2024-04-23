import { Grid, TextField } from '@mui/material';
import PropTypes from 'prop-types';

const TextFieldComponent = ({ defaultValue, required, label, name, type }) => {
  return (
    <Grid item xs={12}>
      <TextField
        sx={{ backgroundColor: 'secondary.main' }}
        defaultValue={defaultValue}
        required={required}
        fullWidth
        label={label}
        name={name}
        type={type}
        variant="filled"
      />
    </Grid>
  );
};

TextFieldComponent.propTypes = {
  defaultValue: PropTypes.string,
  required: PropTypes.bool,
  label: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
  type: PropTypes.string.isRequired,
};

export default TextFieldComponent;
