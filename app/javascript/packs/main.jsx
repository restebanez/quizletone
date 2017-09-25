import React, { Component } from 'react'
import ReactDOM from 'react-dom'
import { Provider,connect } from 'react-redux'
import { createStore, applyMiddleware } from 'redux'
import thunkMiddleware from 'redux-thunk'
import { createLogger } from 'redux-logger'

export const REQUEST_SETS = 'REQUEST_SETS'
export const RECEIVE_SETS_SUCCESS = 'RECEIVE_SETS_SUCCESS'
export const RECEIVE_SETS_FAILURE = 'RECEIVE_SETS_FAILURE'

const rootReducer = (state = {sets: {fetching: false, action: "Never fetched", items: []}}, action) => {
    switch(action.type){
        case REQUEST_SETS:
            return {...state,
                    sets: {...state.sets,
                           action: "Fetching sets...",
                           fetching: true}}
        case RECEIVE_SETS_SUCCESS:
            return {...state,
                    sets: {...state.sets,
                           action: "Sets fetched",
                           items: action.items,
                           fetching: false}}
        default:
            return state
    }
}

const loggerMiddleware = createLogger()

function configureStore(preloadedState) {
    return createStore(
        rootReducer,
        preloadedState,
        applyMiddleware(
            thunkMiddleware,
            loggerMiddleware
        )
    )
}




function requestSets(){
    return {type: REQUEST_SETS}
}

function receiveSetsSuccess(jsonSets){
    return {
        type: RECEIVE_SETS_SUCCESS,
        items: jsonSets.sets
    }
}

function fetchSets(){
    return dispatch => {
        dispatch(requestSets())
        return fetch("/api/v0/sets")
            .then(response => response.json())
            .then(jsonSets => dispatch(receiveSetsSuccess(jsonSets)))
    }
}

const SetList = (props) => (
    <ul>
        {props.items.map((item, i) =>
            <li key={i}>
                <a href={item.url}>{item.title}</a> <br/>
                {item.url}
            </li>)}
    </ul>)


class Ui extends Component {
    constructor(props) {
        super(props)
        this.handleRefreshClick = this.handleRefreshClick.bind(this)
    }

    componentDidMount() {
        this.props.dispatch(fetchSets())
    }

    handleRefreshClick(e) {
        e.preventDefault()
        this.props.dispatch(fetchSets())
    }

    render(){
        const { action, fetching, items } = this.props.sets
        return (
            <div>
                <h2>List of sets {fetching && " Loading..."}</h2>
                <div>
                    <p>{action}</p>
                    <button onClick={this.handleRefreshClick}>Fetch Sets</button>
                    <SetList items={items}/>
                </div>
            </div>)
    }
}

function mapStateToProps(state){
    return {sets: state.sets}
}

const AsyncUI = connect(mapStateToProps)(Ui)


const store = configureStore()

class Root extends Component {
    render() {
        return (
            <Provider store={store}>
                <AsyncUI />
            </Provider>
        )
    }
}

document.addEventListener('DOMContentLoaded', () => {
    ReactDOM.render(
        <Root  />,
        document.body.appendChild(document.createElement('div')),
    )
})
