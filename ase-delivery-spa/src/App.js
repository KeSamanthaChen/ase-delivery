import React from 'react';
import { Layout } from 'antd';
import AppHeader from './components/AppHeader.js';
import { useSelector } from 'react-redux';
import LoginPage from './components/LoginPage/LoginPage'
import CustomerSPA from './components/CustomerSPA/CustomerSPA.js';
import DelivererSPA from './components/DelivererSPA/DelivererSPA.js';
import DispatcherSPA from './components/DispatcherSPA/DispatcherSPA.js';
import './App.less';
// import { Counter } from './components/redux-demo/counter';

function App() {
  const { Header, Content } = Layout;
  const isLoggedIn = useSelector((state) => state.login.isLoggedIn);
  const role = useSelector((state) => state.login.userRole);

  return (
    <div>
      <Header id="header" style={{position: "fixed", zIndex: 100, width: "100%", }}><AppHeader /></Header>
      <Content className="site-layout" id="content" style={{ paddingTop: 64}}>
        {!isLoggedIn && <LoginPage />}
        { (isLoggedIn && role === 'customer') && <CustomerSPA />}
        { (isLoggedIn && role === 'deliverer') && <DelivererSPA />}
        { (isLoggedIn && role === 'dispatcher') && <DispatcherSPA />}
      </Content>
    </div>
  )
}

export default App;
