import React from 'react'
import ReactDOM from 'react-dom'

const SetList = () => (
    <ul>
        <li>
            <a href="#">First item</a> <br/>
            /blalb/baaa
        </li>
        <li>
            <a href="#">Second item</a> <br/>
            /vvvv/jjjj
        </li>
    </ul>)

const Ui = () => (
    <div>
        <h2>List of sets</h2>
        <div>
            <p>Fetching...</p>
            <button>Fetch Sets</button>
            <SetList/>
        </div>
    </div>)

document.addEventListener('DOMContentLoaded', () => {
    ReactDOM.render(
        <Ui  />,
        document.body.appendChild(document.createElement('div')),
    )
})
