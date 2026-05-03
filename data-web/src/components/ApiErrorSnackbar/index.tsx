import Alert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import { useDispatch, useSelector } from 'react-redux';
import { hideApiErrorSnackbar } from '../../features/apiErrorSnackbar/slice';
import type { RootState } from '../../store';

const AUTO_HIDE_MS = 6000;

const ApiErrorSnackbar = () => {
  const dispatch = useDispatch();
  const { open, message } = useSelector((state: RootState) => state.apiErrorSnackbar);

  return (
    <Snackbar
      open={open}
      autoHideDuration={AUTO_HIDE_MS}
      anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
      onClose={() => dispatch(hideApiErrorSnackbar())}
    >
      <Alert
        onClose={() => dispatch(hideApiErrorSnackbar())}
        severity="error"
        variant="filled"
        sx={{ width: '100%' }}
      >
        {message}
      </Alert>
    </Snackbar>
  );
};

export default ApiErrorSnackbar;
