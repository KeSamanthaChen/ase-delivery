import { configureStore } from "@reduxjs/toolkit";
import loginReducer from "./components/LoginPage/loginSlice";
import dispatcherReducer from "./components/DispatcherSPA/dispatcherSlice";
import usersReducer from "../src/components/DispatcherSPA/UsersSlice";
import deliveryReducer from "../src/components/DispatcherSPA/deliverySlice";
import boxReducer from "../src/components/DispatcherSPA/boxSlice";

export default configureStore({
    reducer: {
        login: loginReducer,
        dispatcher: dispatcherReducer,
        users: usersReducer,
        delivery: deliveryReducer,
        box: boxReducer,
    },
});
