import Img from '../layout/ui/img';

const LogoComponent = () => {
  return (
    <Img
      src="../../../assests/BB_logo.png"
      alt="Bill Basher Logo"
      sx={{
        aspectRatio: '1.42',
        width: 1 / 2,
        marginLeft: 'auto',
        marginRight: 'auto',
      }}
    />
  );
};

export default LogoComponent;
