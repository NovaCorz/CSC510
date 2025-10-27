import React, { useState } from 'react';
import BudgetQuestion from './components/BudgetQuestion';
import DietaryRestrictions from './components/DietaryRestrictions';

function App() {
  const [currentStep, setCurrentStep] = useState(1);
  const [quizData, setQuizData] = useState({
    budget: '',
    customBudget: '',
    dietaryRestrictions: [],
    customDietary: ''
  });

  const handleNext = () => {
    if (currentStep < 2) {
      setCurrentStep(currentStep + 1);
    }
  };

  const handlePrevious = () => {
    if (currentStep > 1) {
      setCurrentStep(currentStep - 1);
    }
  };

  const updateQuizData = (field, value) => {
    setQuizData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const renderCurrentStep = () => {
    switch (currentStep) {
      case 1:
        return (
          <BudgetQuestion
            budget={quizData.budget}
            customBudget={quizData.customBudget}
            onUpdate={updateQuizData}
            onNext={handleNext}
            onPrevious={handlePrevious}
            canGoNext={quizData.budget !== '' || quizData.customBudget !== ''}
          />
        );
      case 2:
        return (
          <DietaryRestrictions
            restrictions={quizData.dietaryRestrictions}
            customDietary={quizData.customDietary}
            onUpdate={updateQuizData}
            onNext={handleNext}
            onPrevious={handlePrevious}
            canGoNext={true}
          />
        );
      default:
        return null;
    }
  };

  return (
    <div className="App">
      {renderCurrentStep()}
    </div>
  );
}

export default App;
