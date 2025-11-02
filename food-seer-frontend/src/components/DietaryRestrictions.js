import React, { useState } from 'react';

const DietaryRestrictions = ({ restrictions, onUpdate, onNext, onPrevious, canGoNext }) => {
  const [selectedRestrictions, setSelectedRestrictions] = useState(restrictions);

  const dietaryOptions = [
    { value: 'vegan', label: 'Vegan', description: 'No animal products' },
    { value: 'vegetarian', label: 'Vegetarian', description: 'No meat or fish' },
    { value: 'gluten-free', label: 'Gluten Free', description: 'No gluten or wheat' }
  ];

  const handleRestrictionChange = (value) => {
    let newRestrictions;
    if (selectedRestrictions.includes(value)) {
      newRestrictions = selectedRestrictions.filter(r => r !== value);
    } else {
      newRestrictions = [...selectedRestrictions, value];
    }
    
    setSelectedRestrictions(newRestrictions);
    onUpdate('dietaryRestrictions', newRestrictions);
  };

  const handleNext = () => {
    if (canGoNext) {
      onNext();
    }
  };

  return (
    <div className="quiz-container">
      <div className="icon-container">
        <div className="icon-carrot">🥕</div>
      </div>
      
      <h1 className="question-title">Do you have any dietary restrictions?</h1>
      <p className="question-subtitle">Select all that apply, or skip to see all options</p>
      
      <div className="options-container">
        {dietaryOptions.map((option) => (
          <div
            key={option.value}
            className={`option-card ${selectedRestrictions.includes(option.value) ? 'selected' : ''}`}
            onClick={() => handleRestrictionChange(option.value)}
          >
            <div className={`checkbox-button ${selectedRestrictions.includes(option.value) ? 'selected' : ''}`}></div>
            <div className="option-text">
              <div className="option-label">{option.label}</div>
              <div className="option-description">{option.description}</div>
            </div>
          </div>
        ))}
      </div>
      
      <div className="navigation">
        <button className="previous-button" onClick={onPrevious}>
          <span className="previous-icon">←</span>
          Previous
        </button>
        <button 
          className="next-button" 
          onClick={handleNext}
          disabled={!canGoNext}
        >
          Finish
          <span className="next-icon">✓</span>
        </button>
      </div>
    </div>
  );
};

export default DietaryRestrictions;
