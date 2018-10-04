import React from 'react'
import { Dropdown } from 'semantic-ui-react'

const options = [
  { key: 1, text: 'Male', value: 1 },
  { key: 2, text: 'Female', value: 2 },
  { key: 3, text: 'Other', value: 3 },
]

const Gender = () => (
    
  <Dropdown
    
    search
    selection
    wrapSelection={false}
    options={options}
    placeholder='Sex'
    
  />
)

export default Gender