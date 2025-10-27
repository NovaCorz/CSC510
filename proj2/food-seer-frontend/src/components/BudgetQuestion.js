import React, { useState } from 'react';

const BudgetQuestion = ({ budget, customBudget, onUpdate, onNext, onPrevious, canGoNext }) => {
  const [selectedBudget, setSelectedBudget] = useState(budget);
  const [customValue, setCustomValue] = useState(customBudget);

  const budgetOptions = [
    { value: 'under-10', label: 'Under $10' },
    { value: 'under-20', label: 'Under $20' },
    { value: 'under-30', label: 'Under $30' },
    { value: 'other', label: 'Other' }
  ];

  const handleBudgetChange = (value) => {
    setSelectedBudget(value);
    onUpdate('budget', value);
    
    if (value !== 'other') {
      onUpdate('customBudget', '');
      setCustomValue('');
    }
  };

  const handleCustomInputChange = (e) => {
    const value = e.target.value;
    setCustomValue(value);
    onUpdate('customBudget', value);
  };

  const handleNext = () => {
    if (canGoNext) {
      onNext();
    }
  };

  return (
    <div className="quiz-container">
      <div className="icon-container">
        <div className="icon-dollar">$</div>
      </div>
      
      <h1 className="question-title">How much are you willing to spend per meal?</h1>
      
      <div className="options-container">
        {budgetOptions.map((option) => (
          <div
            key={option.value}
            className={`option-card ${selectedBudget === option.value ? 'selected' : ''}`}
            onClick={() => handleBudgetChange(option.value)}
          >
            <div className={`radio-button ${selectedBudget === option.value ? 'selected' : ''}`}></div>
            <div className="option-text">
              {option.label}
              {option.value === 'other' && (
                <input
                  type="text"
                  className="custom-input"
                  placeholder="Anniversary meal, no limit"
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

export default BudgetQuestion;
