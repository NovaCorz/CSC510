import React, { useState } from 'react';

const DietaryRestrictions = ({ restrictions, customDietary, onUpdate, onNext, onPrevious, canGoNext }) => {
  const [selectedRestrictions, setSelectedRestrictions] = useState(restrictions);
  const [customValue, setCustomValue] = useState(customDietary);

  const dietaryOptions = [
    { value: 'vegan', label: 'Vegan' },
    { value: 'lactose-intolerant', label: 'Lactose intolerant' },
    { value: 'vegetarian', label: 'Vegetarian' },
    { value: 'other', label: 'Other' }
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
    
    if (value === 'other' && !selectedRestrictions.includes('other')) {
      // If "other" is being selected, keep custom value
    } else if (value === 'other' && selectedRestrictions.includes('other')) {
      // If "other" is being deselected, clear custom value
      onUpdate('customDietary', '');
      setCustomValue('');
    }
  };

  const handleCustomInputChange = (e) => {
    const value = e.target.value;
    setCustomValue(value);
    onUpdate('customDietary', value);
    
    // Auto-select "other" when typing in custom input
    if (value && !selectedRestrictions.includes('other')) {
      const newRestrictions = [...selectedRestrictions, 'other'];
      setSelectedRestrictions(newRestrictions);
      onUpdate('dietaryRestrictions', newRestrictions);
    }
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
      
      <h1 className="question-title">What are your dietary restrictions?</h1>
      
      <div className="options-container">
        {dietaryOptions.map((option) => (
          <div
            key={option.value}
            className={`option-card ${selectedRestrictions.includes(option.value) ? 'selected' : ''}`}
            onClick={() => handleRestrictionChange(option.value)}
          >
            <div className={`checkbox-button ${selectedRestrictions.includes(option.value) ? 'selected' : ''}`}></div>
            <div className="option-text">
              {option.label}
              {option.value === 'other' && (
                <input
                  type="text"
                  className="custom-input"
                  placeholder="Pescatarian, no cheese please"
                  value={customValue}
                  onChange={handleCustomInputChange}
                  onClick={(e) => e.stopPropagation()}
                />
              )}
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
          Next
          <span className="next-icon">→</span>
        </button>
      </div>
    </div>
  );
};

export default DietaryRestrictions;
