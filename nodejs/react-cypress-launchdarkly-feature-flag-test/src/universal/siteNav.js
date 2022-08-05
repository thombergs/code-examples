import React from 'react';
import { css } from 'styled-components';
import { Link } from 'react-router-dom';

export default () => (
  <div
    css={css`
      display: flex;
      width: 170px;
      justify-content: space-between;
    `}
  >
    <Link to="/">Home</Link>
    <Link to="/hooks">Hooks Demo</Link>
  </div>
);
