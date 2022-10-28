import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from 'components/Navbar';
import Footer from 'components/Footer';
import DadosPessoais from "pages/DadosPessoais";
import DadosPessoaisList from "pages/DadosPessoais/List";
import DadosPessoaisForm from "pages/DadosPessoais/Form";
import Interesses from "pages/Interesses";
import InteressesList from "pages/Interesses/List";
import InteressesForm from "pages/Interesses/Form";

export default function App() {
  return (
    <div className="d-flex flex-column h-100">
      <BrowserRouter>
        <Navbar />
        <div className="container-lg mb-3">
          <Routes >
            <Route path="/pessoas" element={<DadosPessoais />}>
              <Route path="novo" element={<DadosPessoaisForm />} />
              <Route path="editar/:id" element={<DadosPessoaisForm />} />
              <Route path="" element={<DadosPessoaisList />} />
            </Route>
            <Route path="/interesses" element={<Interesses />}>
              <Route path="novo" element={<InteressesForm />} />
              <Route path="editar/:id" element={<InteressesForm />} />
              <Route path="" element={<InteressesList />} />
            </Route>
          </Routes>
        </div>
        <Footer />
      </BrowserRouter>
    </div>
  );
}
