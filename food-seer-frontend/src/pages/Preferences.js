import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import BudgetQuestion from '../components/BudgetQuestion';
import DietaryRestrictions from '../components/DietaryRestrictions';
import { updateUserPreferences } from '../services/api';

function Preferences() {
  const [currentStep, setCurrentStep] = useState(1);
  const [quizData, setQuizData] = useState({
    budget: '',
    customBudget: '',
    dietaryRestrictions: [],
    customDietary: ''
  });
  const [saving, setSaving] = useState(false);
  const navigate = useNavigate();

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

  const handleFinish = async () => {
    setSaving(true);
    try {
      // Prepare cost preference
      let costPreference = quizData.budget;
      if (quizData.budget === 'other' && quizData.customBudget) {
        costPreference = quizData.customBudget;
      }

      // Prepare dietary restrictions
      let dietaryRestrictions = quizData.dietaryRestrictions.join(', ');
      if (quizData.customDietary) {
        dietaryRestrictions += (dietaryRestrictions ? ', ' : '') + quizData.customDietary;
      }

      // Save preferences to backend
      await updateUserPreferences(costPreference, dietaryRestrictions);

      // Navigate to recommendations
      navigate('/recommendations');
    } catch (error) {
      console.error('Error saving preferences:', error);
      alert('Failed to save preferences. Please try again.');
    } finally {
      setSaving(false);
    }
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
            onNext={handleFinish}
            onPrevious={handlePrevious}
            canGoNext={!saving}
            isLastStep={true}
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

export default Preferences;

