import React, { useState } from 'react';

const BudgetQuestion = ({ budget, onUpdate, onNext, onPrevious, canGoNext }) => {
  const [selectedBudget, setSelectedBudget] = useState(budget);

  const budgetOptions = [
    { value: 'budget', label: 'Budget (Under $10)', description: 'Affordable options' },
    { value: 'moderate', label: 'Moderate ($10-$20)', description: 'Mid-range pricing' },
    { value: 'premium', label: 'Premium ($20+)', description: 'High-end options' },
    { value: 'no-limit', label: 'No Limit', description: 'Show me everything' }
  ];

  const handleBudgetChange = (value) => {
    setSelectedBudget(value);
    onUpdate('budget', value);
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
          Next
          <span className="next-icon">→</span>
        </button>
      </div>
    </div>
  );
};

export default BudgetQuestion;
