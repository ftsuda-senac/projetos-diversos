import { render, screen } from '@testing-library/react';
import App from './App';

test('renders TADS DSW link', () => {
  render(<App />);
  const linkElement = screen.getByText(/TADS DSW/i);
  expect(linkElement).toBeInTheDocument();
});
